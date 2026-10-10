import { Pressable, StyleSheet, Text, View } from 'react-native';

type QuizResultScreenProps = {
  petName: string;
  weightKg: number | null;
  activityLevel: string | null;
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
  5: 'مناسب',
  7: 'کمی اضافه‌وزن',
};

export function QuizResultScreen({
  petName,
  weightKg,
  activityLevel,
  bodyConditionScore,
  dietaryRestrictions,
}: QuizResultScreenProps) {
  const activityLabel = activityLevel
  ? activityLabels[activityLevel] ?? 'نامشخص'
  : 'نامشخص';

const bodyConditionLabel =
  bodyConditionScore !== null
    ? bodyConditionLabels[bodyConditionScore] ?? 'نامشخص'
    : 'نامشخص';
    const dietaryRestrictionLabels: Record<string, string> = {
  CHICKEN: 'مرغ',
  BEEF: 'گوشت گاو',
  FISH: 'ماهی',
  DAIRY: 'لبنیات',
  GRAINS: 'غلات',
  OTHER: 'سایر',
};

const dietaryRestrictionLabel =
  dietaryRestrictions.includes('NONE')
    ? 'موردی ثبت نشده'
    : dietaryRestrictions
        .map(item => dietaryRestrictionLabels[item] ?? item)
        .join('، ');
  return (
    <View style={styles.container}>
      <Text style={styles.title}>
        {petName} رو شناختیم! 🐾
      </Text>

      <Text style={styles.subtitle}>
        حالا می‌تونیم اطلاعاتش رو برای پیشنهادهای شخصی‌سازی‌شده استفاده کنیم.
      </Text>
      <Text style={styles.info}>
  وزن: {weightKg ?? 'نامشخص'} کیلو
</Text>

<Text style={styles.info}>
  فعالیت: {activityLabel}
</Text>

<Text style={styles.info}>
  وضعیت بدن: {bodyConditionLabel}
</Text>
<Text style={styles.info}>
  حساسیت غذایی: {dietaryRestrictionLabel}
</Text>
<Pressable style={styles.continueButton}>
  <Text style={styles.continueButtonText}>
    ادامه
  </Text>
</Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 24,
    direction: 'rtl',
  },
  title: {
    fontSize: 32,
    lineHeight: 44,
    textAlign: 'center',
    marginBottom: 16,
  },
  subtitle: {
    fontSize: 17,
    lineHeight: 28,
    textAlign: 'center',
    color: '#666',
  },
  info: {
  fontSize: 18,
  marginTop: 12,
  textAlign: 'right',
},
continueButton: {
  marginTop: 32,
  paddingVertical: 16,
  paddingHorizontal: 48,
  borderRadius: 12,
  backgroundColor: '#000',
},

continueButtonText: {
  color: '#fff',
  fontSize: 18,
  fontWeight: '600',
},
});