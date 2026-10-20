import { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import { apiPostAuthenticated } from '../../services/api/client';

import type {
  ActivityLevel,
  NeuteredStatus,
  PetSpecies,
} from '../../services/quiz/calorieCalculator';

type CalorieEstimate = {
  rerKcal: number;
  factor: number;
  merKcal: number;
};


type QuizResultScreenProps = {
   petId: number | null;
  petName: string;
  species: PetSpecies | null;
  birthDate: string | null;
  neutered: NeuteredStatus | null;
  weightKg: number | null;
  activityLevel: ActivityLevel | null;
  bodyConditionScore: number | null;
  dietaryRestrictions: string[];
};

const activityLabels: Record<string, string> = {
  LOW: 'کم‌تحرک',
  MODERATE: 'معمولی',
  HIGH: 'فعال',
  VERY_HIGH: 'خیلی فعال',
};

const bodyConditionLabels: Record<number, string> = {
  3: 'لاغر',
  5: 'متناسب',
  7: 'کمی اضافه‌وزن',
};

const dietaryRestrictionLabels: Record<string, string> = {
  CHICKEN: 'مرغ',
  BEEF: 'گوشت گاو',
  FISH: 'ماهی',
  DAIRY: 'لبنیات',
  GRAINS: 'غلات',
  OTHER: 'سایر',
};

export function QuizResultScreen({
  petId,
  petName,
  species,
  birthDate,
  neutered,
  weightKg,
  activityLevel,
  bodyConditionScore,
  dietaryRestrictions,
}: QuizResultScreenProps) {
  const activityLabel = activityLevel
    ? activityLabels[activityLevel]
    : 'نامشخص';

  const bodyConditionLabel =
    bodyConditionScore !== null
      ? bodyConditionLabels[bodyConditionScore] ?? 'نامشخص'
      : 'نامشخص';

  const dietaryRestrictionLabel =
    dietaryRestrictions.includes('NONE')
      ? 'موردی ثبت نشده'
      : dietaryRestrictions
          .map(item => dietaryRestrictionLabels[item] ?? item)
          .join('، ');
const [calorieEstimate, setCalorieEstimate] =
  useState<CalorieEstimate | null>(null);

const [loadingCalories, setLoadingCalories] = useState(false);
const [calorieError, setCalorieError] = useState<string | null>(null);

useEffect(() => {
  if (
    petId === null ||
    !species ||
    !birthDate ||
    neutered === null ||
    neutered === 'UNKNOWN' ||
    weightKg === null ||
    !activityLevel
  ) {
    setCalorieEstimate(null);
    setCalorieError(null);
    setLoadingCalories(false);
    return;
  }

  let cancelled = false;

  async function loadCalories() {
    try {
      setLoadingCalories(true);
      setCalorieError(null);

      const result = await apiPostAuthenticated<
        {
          petId: number;
          species: PetSpecies;
          weightKg: number;
          birthDate: string;
          neutered: boolean;
          activityLevel: ActivityLevel;
        },
        CalorieEstimate
      >('/calories/calculate', {
        petId,
        species,
        weightKg,
        birthDate,
        neutered,
        activityLevel,
      });

      if (!cancelled) {
        setCalorieEstimate(result);
      }
    } catch (error) {
      if (!cancelled) {
        setCalorieEstimate(null);
        setCalorieError(
          error instanceof Error
            ? error.message
            : 'دریافت کالری ناموفق بود.',
        );
      }
    } finally {
      if (!cancelled) {
        setLoadingCalories(false);
      }
    }
  }

  void loadCalories();

  return () => {
    cancelled = true;
  };
}, [
  petId,
  species,
  birthDate,
  neutered,
  weightKg,
  activityLevel,
]);


  return (
    <View style={styles.container}>
      <Text style={styles.title}>
        {petName} رو شناختیم! 🐾
      </Text>

      <Text style={styles.subtitle}>
        اطلاعات پت ثبت شد و می‌تونیم یک تخمین اولیه از نیاز روزانه‌اش
        داشته باشیم.
      </Text>

      <Text style={styles.info}>
        وزن: {weightKg ?? 'نامشخص'} کیلوگرم
      </Text>

      <Text style={styles.info}>
        فعالیت: {activityLabel}
      </Text>

      <Text style={styles.info}>
        وضعیت بدنی: {bodyConditionLabel}
      </Text>

      <Text style={styles.info}>
        حساسیت غذایی: {dietaryRestrictionLabel}
      </Text>

      {loadingCalories ? (
  <View style={styles.calorieCard}>
    <ActivityIndicator color="#237A4B" />
    <Text style={styles.info}>در حال محاسبه کالری...</Text>
  </View>
) : calorieEstimate ? (
  <View style={styles.calorieCard}>
    <Text style={styles.calorieTitle}>کالری تخمینی روزانه</Text>

    <Text style={styles.calorieValue}>
      حدود {calorieEstimate.merKcal}
    </Text>

    <Text style={styles.calorieUnit}>کیلوکالری در روز</Text>

    <Text style={styles.rerText}>
      انرژی پایه در حالت استراحت: حدود {calorieEstimate.rerKcal}
      {' '}کیلوکالری در روز
    </Text>

    <Text style={styles.rerText}>
      ضریب محاسبه: {calorieEstimate.factor}
    </Text>

    <Text style={styles.disclaimer}>
      این مقدار تخمینی است و نیاز واقعی پت ممکن است متفاوت باشد.
      برای برنامه تغذیه دقیق با دامپزشک مشورت کن.
    </Text>
  </View>
) : (
  <Text style={styles.info}>
    {calorieError ??
      'برای محاسبه کالری، اطلاعات پت باید کامل باشد.'}
  </Text>
)}

      <Pressable style={styles.continueButton}>
        <Text style={styles.continueButtonText}>ادامه</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 24,
    paddingVertical: 32,
    direction: 'rtl',
    backgroundColor: '#FFFFFF',
  },
  title: {
    fontSize: 30,
    lineHeight: 42,
    textAlign: 'center',
    marginBottom: 16,
    color: '#17221B',
  },
  subtitle: {
    fontSize: 16,
    lineHeight: 27,
    textAlign: 'center',
    color: '#68736C',
    marginBottom: 20,
  },
  info: {
    fontSize: 17,
    lineHeight: 28,
    marginTop: 8,
    textAlign: 'right',
    color: '#34443A',
  },
  calorieCard: {
    marginTop: 24,
    padding: 20,
    borderRadius: 16,
    backgroundColor: '#F0F7F2',
    borderWidth: 1,
    borderColor: '#D7E9DC',
  },
  calorieTitle: {
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
    color: '#237A4B',
  },
  calorieValue: {
    marginTop: 12,
    fontSize: 32,
    fontWeight: '700',
    textAlign: 'center',
    color: '#17221B',
  },
  calorieUnit: {
    marginTop: 4,
    fontSize: 14,
    textAlign: 'center',
    color: '#68736C',
  },
  rerText: {
    marginTop: 16,
    fontSize: 14,
    lineHeight: 23,
    textAlign: 'right',
    color: '#34443A',
  },
  disclaimer: {
    marginTop: 12,
    fontSize: 13,
    lineHeight: 22,
    textAlign: 'right',
    color: '#68736C',
  },
  continueButton: {
    marginTop: 28,
    paddingVertical: 16,
    paddingHorizontal: 48,
    borderRadius: 12,
    backgroundColor: '#237A4B',
    alignItems: 'center',
  },
  continueButtonText: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: '600',
  },
});
