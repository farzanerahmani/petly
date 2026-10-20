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

type WeightRecord = {
  id: number;
  petId: number;
  weightKg: number;
  recordedDate: string;
  createdAt: string;
};

type BodyConditionRecord = {
  id: number;
  petId: number;
  score: number;
  recordedDate: string;
  createdAt: string;
};

type TimelineItem =
  | {
      type: 'weight';
      id: number;
      date: string;
      weightKg: number;
    }
  | {
      type: 'condition';
      id: number;
      date: string;
      score: number;
    };

function formatDate(value: string): string {
  const parts = value.split('-');
  return parts.length === 3
    ? `${parts[2]}/${parts[1]}/${parts[0]}`
    : value;
}

function getConditionLabel(score: number): string {
  if (score <= 3) return 'احتمال کمبود وزن';
  if (score <= 5) return 'محدوده‌ی متناسب';
  if (score === 6) return 'احتمال اضافه‌وزن';
  return 'احتمال اضافه‌وزن قابل‌توجه';
}

export default function WeightRecordsScreen() {
  const { petId } = useLocalSearchParams<{ petId: string }>();

  const [items, setItems] = useState<TimelineItem[]>([]);
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

      const [weights, conditions] = await Promise.all([
        apiGet<WeightRecord[]>(`/pets/${petId}/weight-records`, token),
        apiGet<BodyConditionRecord[]>(
          `/pets/${petId}/body-condition-records`,
          token,
        ),
      ]);

      const timeline: TimelineItem[] = [
        ...weights.map((record) => ({
          type: 'weight' as const,
          id: record.id,
          date: record.recordedDate,
          weightKg: record.weightKg,
        })),
        ...conditions.map((record) => ({
          type: 'condition' as const,
          id: record.id,
          date: record.recordedDate,
          score: record.score,
        })),
      ];

      timeline.sort((a, b) => b.date.localeCompare(a.date));
      setItems(timeline);
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : 'دریافت سوابق ناموفق بود.',
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
        <Text style={styles.muted}>در حال دریافت سوابق...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Pressable onPress={() => router.back()} style={styles.backButton}>
        <Text style={styles.backText}>→ بازگشت به پروفایل</Text>
      </Pressable>

      <Text style={styles.title}>وزن و وضعیت بدنی</Text>
      <Text style={styles.description}>
        همه‌ی سوابق وزن و ارزیابی وضعیت بدنی در یک فهرست نمایش داده می‌شوند.
      </Text>

      {error ? (
        <>
          <Text style={styles.error}>{error}</Text>
          <Pressable style={styles.button} onPress={() => void loadRecords()}>
            <Text style={styles.buttonText}>تلاش دوباره</Text>
          </Pressable>
        </>
      ) : items.length === 0 ? (
        <Text style={styles.muted}>هنوز رکوردی ثبت نشده است.</Text>
      ) : (
        <ScrollView contentContainerStyle={styles.list}>
          {items.map((item, index) => (
            <View key={item.id ?? index} style={styles.card}>
              <Text style={styles.date}>{formatDate(item.date)}</Text>

              {item.type === 'weight' ? (
                <>
                  <Text style={styles.label}>وزن ثبت‌شده</Text>
                  <Text style={styles.value}>
                    {item.weightKg} کیلوگرم
                  </Text>
                </>
              ) : (
                <>
                  <Text style={styles.label}>ارزیابی وضعیت بدنی</Text>
                  <Text style={styles.value}>
                    {getConditionLabel(item.score)}
                  </Text>
                  <Text style={styles.note}>
                    این عبارت بر اساس امتیاز ثبت‌شده است و تشخیص قطعی دامپزشکی نیست.
                  </Text>
                </>
              )}
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
  date: {
    fontSize: 13,
    color: '#68736C',
    textAlign: 'right',
    marginBottom: 12,
  },
  label: {
    fontSize: 14,
    color: '#68736C',
    textAlign: 'right',
  },
  value: {
    fontSize: 19,
    fontWeight: '700',
    color: '#17221B',
    textAlign: 'right',
    marginTop: 6,
  },
  note: {
    fontSize: 12,
    color: '#68736C',
    textAlign: 'right',
    marginTop: 8,
    lineHeight: 18,
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
measurementCard: {
  borderWidth: 1,
  borderColor: '#D7DED9',
  borderRadius: 14,
  backgroundColor: '#FFFFFF',
  overflow: 'hidden',
  marginBottom: 16,
},

measurementRow: {
  flexDirection: 'row',
  alignItems: 'stretch',
},

measurementField: {
  flex: 1,
  minWidth: 0,
  padding: 14,
  justifyContent: 'center',
},

fieldDivider: {
  width: 1,
  backgroundColor: '#E4E9E5',
  marginVertical: 12,
},

fieldLabel: {
  fontSize: 14,
  fontWeight: '600',
  color: '#34443A',
  marginBottom: 10,
},
});