import { useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { router, useLocalSearchParams } from 'expo-router';
import { apiGet, apiPost } from '../services/api/client';
import { saveAccessToken } from '../services/auth/tokenStorage';
import type { PetResponseDto } from '../types/pet/PetResponseDto';

type VerifyOtpResponse = {
  userId: number;
  phoneNumber: string;
  newUser: boolean;
  accessToken: string;
};

export default function OtpScreen() {
  const { phoneNumber } = useLocalSearchParams<{ phoneNumber: string }>();
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);

  async function verifyOtp() {
    if (!/^\d{6}$/.test(code)) {
      Alert.alert('کد نامعتبر', 'کد تأیید باید دقیقاً ۶ رقم باشد.');
      return;
    }

    if (!phoneNumber) {
      Alert.alert('خطا', 'شماره موبایل پیدا نشد؛ دوباره وارد شو.');
      router.replace('/login');
      return;
    }

    try {
      setLoading(true);

      const result = await apiPost<
        { phoneNumber: string; code: string },
        VerifyOtpResponse
      >('/auth/otp/verify', { phoneNumber, code });

      await saveAccessToken(result.accessToken);

      const pets = await apiGet<PetResponseDto[]>(
        '/pets',
        result.accessToken,
      );

      if (result.newUser || pets.length === 0) {
        router.replace('/onboarding');
      } else {
        router.replace('/pets');
      }
    } catch (error) {
      Alert.alert(
        'خطا',
        error instanceof Error
          ? error.message
          : 'تأیید کد یا دریافت فهرست حیوان‌ها ناموفق بود.',
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>کد تأیید</Text>
        <Text style={styles.subtitle}>
          کد ۶ رقمی ارسال‌شده به {phoneNumber} را وارد کن.
        </Text>

        <TextInput
          style={styles.input}
          value={code}
          onChangeText={(value) =>
            setCode(value.replace(/\D/g, '').slice(0, 6))
          }
          placeholder="------"
          keyboardType="number-pad"
          textAlign="center"
          maxLength={6}
          editable={!loading}
        />

        <Pressable
          style={[styles.button, loading && styles.disabledButton]}
          onPress={verifyOtp}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.buttonText}>تأیید و ادامه</Text>
          )}
        </Pressable>
      </View>
    </View>
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
    fontSize: 24,
    letterSpacing: 8,
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
