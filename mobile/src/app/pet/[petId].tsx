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

import { apiGet } from '../../services/api/client';
import { getAccessToken } from '../../services/auth/tokenStorage';
import type { PetResponseDto } from '../../types/pet/PetResponseDto';

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

function formatDate(value: string | null): string {
  if (!value) return 'ثبت نشده';

  const parts = value.split('-');
  if (parts.length !== 3) return value;

  return `${parts[2]}/${parts[1]}/${parts[0]}`;
}

function getSpeciesLabel(species: PetResponseDto['species']): string {
  return species === 'DOG' ? 'سگ' : 'گربه';
}

function getGenderLabel(gender: PetResponseDto['gender']): string {
  switch (gender) {
    case 'MALE':
      return 'نر';
    case 'FEMALE':
      return 'ماده';
    default:
      return 'مشخص نشده';
  }
}

function getActivityLabel(
  activity: PetResponseDto['activityLevel'],
): string {
  switch (activity) {
    case 'LOW':
      return 'کم';
    case 'MODERATE':
      return 'متوسط';
    case 'HIGH':
      return 'زیاد';
    case 'VERY_HIGH':
      return 'بسیار زیاد';
    default:
      return 'ثبت نشده';
  }
}

export default function PetProfileScreen() {
  const { petId } = useLocalSearchParams<{ petId: string }>();

  const [pet, setPet] = useState<PetResponseDto | null>(null);
  const [weights, setWeights] = useState<WeightRecord[]>([]);
  const [bodyConditions, setBodyConditions] =
    useState<BodyConditionRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadProfile = useCallback(async () => {
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

      const [petResult, weightResult, bodyConditionResult] =
        await Promise.all([
          apiGet<PetResponseDto>(`/pets/${petId}`, token),
          apiGet<WeightRecord[]>(`/pets/${petId}/weight-records`, token),
          apiGet<BodyConditionRecord[]>(
            `/pets/${petId}/body-condition-records`,
            token,
          ),
        ]);

      setPet(petResult);
      setWeights(weightResult);
      setBodyConditions(bodyConditionResult);
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : 'دریافت اطلاعات پروفایل پت ناموفق بود.',
      );
    } finally {
      setLoading(false);
    }
  }, [petId]);

  useEffect(() => {
    void loadProfile();
  }, [loadProfile]);

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" color="#237A4B" />
        <Text style={styles.muted}>در حال دریافت پروفایل پت...</Text>
      </View>
    );
  }

  if (error || !pet) {
    return (
      <View style={styles.center}>
        <Text style={styles.error}>
          {error || 'اطلاعات پت پیدا نشد.'}
        </Text>

        <Pressable style={styles.button} onPress={() => router.back()}>
          <Text style={styles.buttonText}>بازگشت</Text>
        </Pressable>

        <Pressable
          style={styles.secondaryButton}
          onPress={() => void loadProfile()}
        >
          <Text style={styles.secondaryButtonText}>تلاش دوباره</Text>
        </Pressable>
      </View>
    );
  }

  const latestWeight = [...weights].sort((a, b) =>
    b.recordedDate.localeCompare(a.recordedDate),
  )[0];

  const latestBodyCondition = [...bodyConditions].sort((a, b) =>
    b.recordedDate.localeCompare(a.recordedDate),
  )[0];

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      <Pressable onPress={() => router.back()} style={styles.backButton}>
        <Text style={styles.backText}>→ بازگشت به حیوان‌های من</Text>
      </Pressable>

      <View style={styles.hero}>
        <View style={styles.avatar}>
          <Text style={styles.avatarText}>
            {pet.species === 'DOG' ? '🐕' : '🐈'}
          </Text>
        </View>

        <Text style={styles.name}>{pet.name}</Text>
        <Text style={styles.subtitle}>{getSpeciesLabel(pet.species)}</Text>
      </View>

      <Text style={styles.sectionTitle}>مشخصات پت</Text>

      <View style={styles.card}>
        <InfoRow label="نام" value={pet.name} />
        <InfoRow label="گونه" value={getSpeciesLabel(pet.species)} />
        <InfoRow label="نژاد" value={pet.breed || 'ثبت نشده'} />
        <InfoRow label="جنسیت" value={getGenderLabel(pet.gender)} />
        <InfoRow label="تاریخ تولد" value={formatDate(pet.birthDate)} />
        <InfoRow
          label="سطح فعالیت"
          value={getActivityLabel(pet.activityLevel)}
        />
      </View>

      <Text style={styles.sectionTitle}>سلامت و وضعیت بدنی</Text>

      <View style={styles.card}>
        <InfoRow
          label="آخرین وزن"
          value={
            latestWeight
              ? `${latestWeight.weightKg} کیلوگرم`
              : 'هنوز ثبت نشده'
          }
        />
        <InfoRow
          label="تاریخ ثبت وزن"
          value={
            latestWeight
              ? formatDate(latestWeight.recordedDate)
              : '—'
          }
        />
        <InfoRow
          label="آخرین امتیاز وضعیت بدنی"
          value={
            latestBodyCondition
              ? `${latestBodyCondition.score} از 9`
              : 'هنوز ثبت نشده'
          }
        />
        <InfoRow
          label="تاریخ ثبت وضعیت بدنی"
          value={
            latestBodyCondition
              ? formatDate(latestBodyCondition.recordedDate)
              : '—'
          }
        />
      </View>

     <Pressable
  style={styles.button}
  onPress={() =>
    router.push({
      pathname: '/pet/[petId]/weight-records',
      params: { petId: String(pet.id) },
    })
  }
>
  <Text style={styles.buttonText}>وزن و وضعیت بدنی</Text>
</Pressable>

      
    </ScrollView>
  );
}

function InfoRow({ label, value }: { label: string; value: string }) {
  return (
    <View style={styles.infoRow}>
      <Text style={styles.infoValue}>{value}</Text>
      <Text style={styles.infoLabel}>{label}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F7F9F7',
  },
  content: {
    padding: 20,
    paddingTop: 56,
    paddingBottom: 40,
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
    fontSize: 14,
    color: '#237A4B',
    fontWeight: '600',
  },
  hero: {
    backgroundColor: '#E8F3EC',
    borderRadius: 20,
    alignItems: 'center',
    padding: 24,
    marginBottom: 24,
  },
  avatar: {
    width: 88,
    height: 88,
    borderRadius: 44,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },
  avatarText: {
    fontSize: 46,
  },
  name: {
    fontSize: 26,
    fontWeight: '700',
    color: '#17221B',
  },
  subtitle: {
    fontSize: 15,
    color: '#68736C',
    marginTop: 6,
  },
  sectionTitle: {
    fontSize: 19,
    fontWeight: '700',
    color: '#17221B',
    textAlign: 'right',
    marginBottom: 12,
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E1E7E2',
    borderRadius: 16,
    paddingHorizontal: 16,
    marginBottom: 24,
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#EEF1EF',
    gap: 12,
  },
  infoLabel: {
    color: '#68736C',
    fontSize: 14,
    textAlign: 'right',
    flexShrink: 0,
  },
  infoValue: {
    color: '#17221B',
    fontSize: 15,
    fontWeight: '600',
    textAlign: 'left',
    flexShrink: 1,
  },
  muted: {
    color: '#68736C',
    marginTop: 12,
  },
  error: {
    color: '#B42318',
    textAlign: 'center',
    marginBottom: 16,
  },
  button: {
    minHeight: 52,
    backgroundColor: '#237A4B',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 16,
    marginBottom: 12,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '600',
  },
  secondaryButton: {
    minHeight: 52,
    borderWidth: 1,
    borderColor: '#237A4B',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 16,
  },
  secondaryButtonText: {
    color: '#237A4B',
    fontSize: 15,
    fontWeight: '600',
  },
});