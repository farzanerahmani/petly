import { useCallback, useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { router, useLocalSearchParams } from 'expo-router';

import { apiGet } from '../../../services/api/client';
import { getAccessToken } from '../../../services/auth/tokenStorage';

type BodyConditionRecord = {
  id: number;
  petId: number;
  score: number;
  recordedDate: string;
  createdAt: string;
};

function formatDate(value: string): string {
  const parts = value.split('-');
  return parts.length === 3
    ? `${parts[2]}/${parts[1]}/${parts[0]}`
    : value;
}

export default function BodyConditionRecordsScreen() {
  const { petId } = useLocalSearchParams<{ petId: string }>();

  const [records, setRecords] = useState<BodyConditionRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadRecords = useCallback(async () => {
    if (!petId || !/^\d+$/.test(petId)) {
      setError('شناسه‌ی پت معتبر نیست.');
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError('');

      const token = await getAccessToken();

      if (!token) {
        router.replace('/login');
        return;
      }

      const result = await apiGet<BodyConditionRecord[]>(
        `/pets/${petId}/body-condition-records`,
        token,
      );

      setRecords(result);
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : 'دریافت سوابق وضعیت بدنی ناموفق بود.',
      );
    } finally {
      setLoading(false);
    }
  }, [petId]);

  useEffect(() => {
    void loadRecords();
  }, [loadRecords]);

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" color="#237A4B" />
        <Text style={styles.muted}>در حال دریافت سوابق وضعیت بدنی...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Pressable onPress={() => router.back()} style={styles.backButton}>
        <Text style={styles.backText}>→ بازگشت به پروفایل</Text>
      </Pressable>

      <Text style={styles.title}>سوابق وضعیت بدنی</Text>
      <Text style={styles.description}>
        امتیاز وضعیت بدنی معمولاً در مقیاس ۱ تا ۹ ثبت می‌شود.
      </Text>

      {error ? (
        <>
          <Text style={styles.error}>{error}</Text>
          <Pressable style={styles.button} onPress={() => void loadRecords()}>
            <Text style={styles.buttonText}>تلاش دوباره</Text>
          </Pressable>
        </>
      ) : records.length === 0 ? (
        <Text style={styles.muted}>هنوز سابقه‌ای ثبت نشده است.</Text>
      ) : (
        <ScrollView contentContainerStyle={styles.list}>
          {records
            .slice()
            .sort((a, b) => b.recordedDate.localeCompare(a.recordedDate))
            .map((record) => (
              <View key={record.id} style={styles.card}>
                <Text style={styles.score}>
                  امتیاز: {record.score} از ۹
                </Text>
                <View style={styles.scoreTrack}>
                  <View
                    style={[
                      styles.scoreFill,
                      { width: `${(record.score / 9) * 100}%` },
                    ]}
                  />
                </View>
                <Text style={styles.date}>
                  تاریخ ثبت: {formatDate(record.recordedDate)}
                </Text>
              </View>
            ))}
        </ScrollView>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F7F9F7',
    padding: 20,
    paddingTop: 56,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    padding: 24,
  },
  backButton: {
    alignSelf: 'flex-end',
    paddingVertical: 10,
    marginBottom: 16,
  },
  backText: {
    color: '#237A4B',
    fontSize: 14,
    fontWeight: '600',
  },
  title: {
    color: '#17221B',
    fontSize: 25,
    fontWeight: '700',
    textAlign: 'right',
    marginBottom: 10,
  },
  description: {
    color: '#68736C',
    fontSize: 14,
    textAlign: 'right',
    lineHeight: 22,
    marginBottom: 20,
  },
  list: {
    paddingBottom: 32,
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E1E7E2',
    borderRadius: 14,
    padding: 18,
    marginBottom: 12,
  },
  score: {
    fontSize: 18,
    fontWeight: '700',
    color: '#17221B',
    textAlign: 'right',
  },
  scoreTrack: {
    height: 8,
    backgroundColor: '#E8EEE9',
    borderRadius: 4,
    overflow: 'hidden',
    marginTop: 12,
  },
  scoreFill: {
    height: '100%',
    backgroundColor: '#237A4B',
    borderRadius: 4,
  },
  date: {
    fontSize: 14,
    color: '#68736C',
    textAlign: 'right',
    marginTop: 12,
  },
  muted: {
    fontSize: 15,
    color: '#68736C',
    textAlign: 'right',
    marginTop: 12,
  },
  error: {
    color: '#B42318',
    textAlign: 'right',
    marginBottom: 16,
  },
  button: {
    minHeight: 48,
    backgroundColor: '#237A4B',
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonText: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
});