import { useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { router } from 'expo-router';
import { apiPost } from '../services/api/client';

type RequestOtpResponse = {
  phoneNumber: string;
  expiresAt: string;
  debugCode?: string;
};

export default function LoginScreen() {
  const [phone, setPhone] = useState('');
  const [loading, setLoading] = useState(false);

  async function requestOtp() {
    const normalizedPhone = phone.replace(/[\s-]/g, '');

    if (!/^09\d{9}$/.test(normalizedPhone)) {
      Alert.alert('شماره نامعتبر', 'شماره موبایل را با 09 و 11 رقم وارد کن.');
      return;
    }

    const phoneNumber = `+98${normalizedPhone.slice(1)}`;

    try {
      setLoading(true);

      await apiPost<
        { phoneNumber: string },
        RequestOtpResponse
      >('/auth/otp/request', { phoneNumber });

      router.push({
        pathname: '/otp',
        params: { phoneNumber },
      });
    } catch (error) {
      Alert.alert(
        'خطا',
        error instanceof Error
          ? error.message
          : 'ارسال کد تأیید ناموفق بود.',
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <View style={styles.content}>
        <Text style={styles.title}>ورود به Petly</Text>
        <Text style={styles.subtitle}>
          شماره موبایلت را وارد کن تا کد تأیید برایت ارسال شود.
        </Text>

        <TextInput
          style={styles.input}
          value={phone}
          onChangeText={setPhone}
          placeholder="09123456789"
          keyboardType="phone-pad"
          textAlign="right"
          maxLength={13}
          editable={!loading}
        />

        <Pressable
          style={[styles.button, loading && styles.disabledButton]}
          onPress={requestOtp}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.buttonText}>دریافت کد تأیید</Text>
          )}
        </Pressable>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    padding: 24,
  },
  content: {
    width: '100%',
    maxWidth: 420,
    alignSelf: 'center',
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    textAlign: 'right',
    color: '#17221B',
    marginBottom: 12,
  },
  subtitle: {
    fontSize: 15,
    lineHeight: 24,
    textAlign: 'right',
    color: '#68736C',
    marginBottom: 28,
  },
  input: {
    borderWidth: 1,
    borderColor: '#D7DED9',
    borderRadius: 12,
    padding: 16,
    fontSize: 18,
    marginBottom: 16,
  },
  button: {
    minHeight: 52,
    backgroundColor: '#237A4B',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  disabledButton: {
    opacity: 0.6,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
});
