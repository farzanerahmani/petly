import { useState } from 'react';
import {
  Button,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import DateTimePicker from '@react-native-community/datetimepicker';

import { QuizSelect } from '../../components/quiz/QuizSelect';
import { BodyConditionOption } from '../../components/quiz/BodyConditionOption';
import { DietaryRestrictionOption } from '../../components/quiz/DietaryRestrictionOption';
import { QuizStep } from '../../components/quiz/QuizStep';

import { QuizResultScreen } from './QuizResultScreen';
import { QuizProcessingScreen } from './QuizProcessingScreen';

import type { QuizData } from '../../types/quiz/QuizTypes';
import type { QuizStep as QuizStepType } from '../../types/quiz/QuizStep';
import type { PetResponseDto } from '../../types/pet/PetResponseDto';

import { apiPostAuthenticated } from '../../services/api/client';

export function QuizScreen() {
  const [currentStep, setCurrentStep] =
    useState<QuizStepType>('PARENT_NAME');

  const [quizData, setQuizData] = useState<QuizData>({
    parentFirstName: '',
    petName: '',
    species: null,
    breed: null,
    birthDate: null,
    gender: null,
    neutered: null,
    weightKg: null,
    activityLevel: null,
    bodyConditionScore: null,
    dietaryRestrictions: [],
  });

  const [error, setError] = useState('');
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const [showResult, setShowResult] = useState(false);

  // برای جلوگیری از ساخت دوباره پت هنگام تلاش مجدد
  // پس از خطا در ذخیره رکوردها
  const [createdPetId, setCreatedPetId] = useState<number | null>(null);
  const [weightRecordSaved, setWeightRecordSaved] = useState(false);
  const [bodyConditionRecordSaved, setBodyConditionRecordSaved] =
    useState(false);

  const breedOptions =
    quizData.species === 'DOG'
      ? [
          { label: 'گلدن رتریور', value: 'Golden Retriever' },
          { label: 'ژرمن شپرد', value: 'German Shepherd' },
          { label: 'لابرادور', value: 'Labrador' },
          { label: 'پودل', value: 'Poodle' },
          { label: 'هاسکی', value: 'Husky' },
          { label: 'نژادش رو نمی‌دونم', value: 'UNKNOWN' },
        ]
      : [
          { label: 'پرشین', value: 'Persian' },
          { label: 'بریتیش شورت‌هیر', value: 'British Shorthair' },
          { label: 'اسکاتیش فولد', value: 'Scottish Fold' },
          { label: 'سیامی', value: 'Siamese' },
          { label: 'نژادش رو نمی‌دونم', value: 'UNKNOWN' },
        ];

  const activityOptions =
    quizData.species === 'DOG'
      ? [
          { label: 'کمتر از ۱ ساعت', value: 'LOW' },
          { label: '۱ تا ۲ ساعت', value: 'MODERATE' },
          { label: '۲ تا ۳ ساعت', value: 'HIGH' },
          { label: 'بیشتر از ۳ ساعت', value: 'VERY_HIGH' },
        ]
      : [
          { label: 'کم‌تحرک', value: 'LOW' },
          { label: 'معمولی', value: 'MODERATE' },
          { label: 'فعال', value: 'HIGH' },
          { label: 'خیلی فعال', value: 'VERY_HIGH' },
        ];

  const dietaryRestrictionOptions = [
    { label: 'مرغ', value: 'CHICKEN' },
    { label: 'گوشت گاو', value: 'BEEF' },
    { label: 'ماهی', value: 'FISH' },
    { label: 'لبنیات', value: 'DAIRY' },
    { label: 'غلات', value: 'GRAINS' },
    { label: 'سایر', value: 'OTHER' },
  ];

  const getQuestion = () => {
    switch (currentStep) {
      case 'PARENT_NAME':
        return 'اول از همه، اسمت چیه؟';

      case 'PET_NAME':
        return 'اسم پتت چیه؟';

      case 'BIRTH_DATE':
        return `${quizData.petName} متولد چه روزیه؟`;

      default:
        return '';
    }
  };

  const saveQuizData = async () => {
    if (
      !quizData.petName.trim() ||
      !quizData.species ||
      !quizData.gender ||
      !quizData.birthDate ||
      !quizData.activityLevel ||
      quizData.weightKg === null ||
      !Number.isFinite(quizData.weightKg) ||
      quizData.weightKg <= 0 ||
      quizData.bodyConditionScore === null
    ) {
      setError('اطلاعات پت کامل نیست یا بعضی از مقادیر معتبر نیستند.');
      return;
    }

    setIsProcessing(true);
    setError('');

    try {
      let petId = createdPetId;



if (petId === null) {
  const pet = await apiPostAuthenticated<
    {
      name: string;
      species: 'DOG' | 'CAT';
      breed: string;
      gender: 'MALE' | 'FEMALE' | 'UNKNOWN';
      birthDate: string;
      activityLevel: 'LOW' | 'MODERATE' | 'HIGH' | 'VERY_HIGH';
      neutered: boolean | null;
    },
    PetResponseDto
  >('/pets', {
    name: quizData.petName.trim(),
    species: quizData.species,
    breed: quizData.breed ?? 'UNKNOWN',
    gender: quizData.gender,
    birthDate: quizData.birthDate,
    activityLevel: quizData.activityLevel,
    neutered: quizData.neutered === 'UNKNOWN'
      ? null
      : quizData.neutered === 'YES',
  });

  // ابتدا شناسه را از پاسخ بک‌اند بگیر
  petId = pet.id;

  // سپس در state ذخیره کن
  setCreatedPetId(petId);

  console.log('Created pet ID:', petId);
}

// بررسی باید بعد از گرفتن شناسه انجام شود
if (petId === null) {
  throw new Error('شناسه پت ایجاد نشده است.');
}


      // تاریخ محلی با فرمت مورد انتظار LocalDate در بک‌اند
      const now = new Date();
      const recordedDate = [
        now.getFullYear(),
        String(now.getMonth() + 1).padStart(2, '0'),
        String(now.getDate()).padStart(2, '0'),
      ].join('-');

      // ثبت وزن
      if (!weightRecordSaved) {
        await apiPostAuthenticated<
          { weightKg: number; recordedDate: string },
          unknown
        >(`/pets/${petId}/weight-records`, {
          weightKg: quizData.weightKg,
          recordedDate,
        });

        setWeightRecordSaved(true);
      }

      // ثبت وضعیت بدنی
      if (!bodyConditionRecordSaved) {
        await apiPostAuthenticated<
          { score: number; recordedDate: string },
          unknown
        >(`/pets/${petId}/body-condition-records`, {
          score: quizData.bodyConditionScore,
          recordedDate,
        });

        setBodyConditionRecordSaved(true);
      }

      setShowResult(true);
    } catch (saveError) {
      console.error('Failed to save quiz data:', saveError);

      setError(
        'ذخیرهٔ اطلاعات با مشکل مواجه شد. لطفاً دوباره تلاش کن.',
      );
    } finally {
      setIsProcessing(false);
    }
  };

  const goToNextStep = () => {
    setError('');

    if (currentStep === 'PARENT_NAME') {
      if (!quizData.parentFirstName.trim()) {
        setError('لطفاً اسمت رو وارد کن.');
        return;
      }

      setCurrentStep('PET_NAME');
      return;
    }

    if (currentStep === 'PET_NAME') {
      if (!quizData.petName.trim()) {
        setError('لطفاً اسم پتت رو وارد کن.');
        return;
      }

      setCurrentStep('SPECIES');
      return;
    }

    if (currentStep === 'SPECIES') {
      if (!quizData.species) {
        setError('لطفاً مشخص کن پتت سگ هست یا گربه.');
        return;
      }

      setQuizData(previous => ({
        ...previous,
        breed: null,
        activityLevel: null,
      }));

      setCurrentStep('BREED');
      return;
    }

    if (currentStep === 'BREED') {
      if (!quizData.breed) {
        setError('لطفاً نژاد پتت رو انتخاب کن.');
        return;
      }

      setCurrentStep('BIRTH_DATE');
      return;
    }

    if (currentStep === 'BIRTH_DATE') {
      if (!quizData.birthDate) {
        setError('لطفاً تاریخ تولد پتت رو انتخاب کن.');
        return;
      }

      setCurrentStep('GENDER');
      return;
    }

    if (currentStep === 'GENDER') {
      if (!quizData.gender) {
        setError('لطفاً جنسیت پتت رو انتخاب کن.');
        return;
      }

      setCurrentStep('NEUTERED');
      return;
    }

    if (currentStep === 'NEUTERED') {
      if (quizData.neutered === null) {
        setError('لطفاً مشخص کن پتت عقیم شده یا نه.');
        return;
      }

      setCurrentStep('WEIGHT');
      return;
    }

    if (currentStep === 'WEIGHT') {
      if (
        quizData.weightKg === null ||
        !Number.isFinite(quizData.weightKg) ||
        quizData.weightKg <= 0
      ) {
        setError('لطفاً وزن معتبر پتت رو وارد کن.');
        return;
      }

      setCurrentStep('ACTIVITY');
      return;
    }

    if (currentStep === 'ACTIVITY') {
      if (!quizData.activityLevel) {
        setError('لطفاً میزان فعالیت پتت رو انتخاب کن.');
        return;
      }

      setCurrentStep('BODY_CONDITION');
      return;
    }

    if (currentStep === 'BODY_CONDITION') {
      if (quizData.bodyConditionScore === null) {
        setError('لطفاً وضعیت بدنی پتت رو انتخاب کن.');
        return;
      }

      setCurrentStep('DIETARY_RESTRICTIONS');
      return;
    }

    if (currentStep === 'DIETARY_RESTRICTIONS') {
      if (quizData.dietaryRestrictions.length === 0) {
        setError('لطفاً حداقل یکی از گزینه‌ها رو انتخاب کن.');
        return;
      }

      void saveQuizData();
    }
  };

  const styles = StyleSheet.create({
    container: {
      flex: 1,
    },
    content: {
      flex: 1,
      justifyContent: 'center',
      padding: 24,
    },
    error: {
      marginBottom: 16,
      fontSize: 16,
      color: '#C62828',
      textAlign: 'right',
    },
    speciesQuestion: {
      flexDirection: 'row',
      alignItems: 'center',
      flexWrap: 'wrap',
      marginBottom: 32,
      direction: 'rtl',
    },
    speciesText: {
      fontSize: 28,
      textAlign: 'right',
    },
    birthDateQuestion: {
      marginBottom: 32,
      direction: 'rtl',
    },
    dateButton: {
      marginTop: 24,
      alignSelf: 'flex-start',
    },
    dateText: {
      fontSize: 28,
      textDecorationLine: 'underline',
    },
    birthDateHint: {
      marginTop: 16,
      fontSize: 16,
      lineHeight: 24,
      textAlign: 'right',
    },
    genderQuestion: {
      marginBottom: 32,
      direction: 'rtl',
    },
    genderTitle: {
      fontSize: 28,
      textAlign: 'right',
      marginBottom: 24,
    },
    neuteredQuestion: {
      marginBottom: 32,
      direction: 'rtl',
    },
    weightQuestion: {
      marginBottom: 32,
      direction: 'rtl',
    },
    weightText: {
      fontSize: 28,
      textAlign: 'right',
    },
    weightInput: {
      minWidth: 90,
      fontSize: 28,
      textAlign: 'center',
      borderBottomWidth: 1,
      paddingVertical: 4,
      marginHorizontal: 8,
    },
    weightHint: {
      marginTop: 20,
      fontSize: 16,
      lineHeight: 24,
      textAlign: 'right',
    },
    activityQuestion: {
      flexDirection: 'row',
      alignItems: 'center',
      flexWrap: 'wrap',
      marginBottom: 32,
      direction: 'rtl',
    },
    activityText: {
      fontSize: 28,
      textAlign: 'right',
    },
    bodyConditionQuestion: {
      marginBottom: 32,
    },
    bodyConditionTitle: {
      fontSize: 28,
      lineHeight: 38,
      textAlign: 'right',
      marginBottom: 24,
    },
    bodyConditionOptions: {
      gap: 12,
    },
    dietaryQuestion: {
      marginBottom: 32,
      direction: 'rtl',
    },
    dietaryTitle: {
      fontSize: 28,
      lineHeight: 38,
      textAlign: 'right',
      marginBottom: 24,
    },
  });


if (showResult) {
  return (
    <QuizResultScreen
    petId={createdPetId}
      petName={quizData.petName}
      species={quizData.species}
      birthDate={quizData.birthDate}
      neutered={quizData.neutered}
      weightKg={quizData.weightKg}
      activityLevel={quizData.activityLevel}
      bodyConditionScore={quizData.bodyConditionScore}
      dietaryRestrictions={quizData.dietaryRestrictions}
    />
  );
}


  if (isProcessing) {
    return (
      <QuizProcessingScreen
        petName={quizData.petName}
        onComplete={() => {}}
      />
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        {currentStep === 'PARENT_NAME' ||
        currentStep === 'PET_NAME' ? (
          <QuizStep
            question={getQuestion()}
            value={
              currentStep === 'PARENT_NAME'
                ? quizData.parentFirstName
                : quizData.petName
            }
            onChange={value =>
              setQuizData(previous => ({
                ...previous,
                ...(currentStep === 'PARENT_NAME'
                  ? { parentFirstName: value }
                  : { petName: value }),
              }))
            }
            showTextInput
          />
        ) : null}

        {currentStep === 'SPECIES' ? (
          <View style={styles.speciesQuestion}>
            <Text style={styles.speciesText}>
              {quizData.petName} یه
            </Text>

            <QuizSelect
              value={quizData.species}
              options={[
                { label: 'سگ', value: 'DOG' },
                { label: 'گربه', value: 'CAT' },
              ]}
              onChange={value =>
                setQuizData(previous => ({
                  ...previous,
                  species: value as 'DOG' | 'CAT',
                }))
              }
            />

            <Text style={styles.speciesText}>هست.</Text>
          </View>
        ) : null}

        {currentStep === 'BREED' ? (
          <View style={styles.speciesQuestion}>
            <Text style={styles.speciesText}>
              {quizData.petName} یه
            </Text>

            <QuizSelect
              value={quizData.breed}
              options={breedOptions}
              onChange={value =>
                setQuizData(previous => ({
                  ...previous,
                  breed: value,
                }))
              }
            />

            <Text style={styles.speciesText}>هست.</Text>
          </View>
        ) : null}

        {currentStep === 'BIRTH_DATE' ? (
          <View style={styles.birthDateQuestion}>
            <Text style={styles.speciesText}>
              {quizData.petName} متولد چه روزیه؟
            </Text>

            <Pressable
              style={styles.dateButton}
              onPress={() => setShowDatePicker(true)}
            >
              <Text style={styles.dateText}>
                {quizData.birthDate ?? 'انتخاب تاریخ'}
              </Text>
            </Pressable>

            <Text style={styles.birthDateHint}>
              تاریخ دقیقش رو نمی‌دونی؟ اشکالی نداره، نزدیک‌ترین تاریخی
              که یادت میاد رو وارد کن.
            </Text>

            {showDatePicker ? (
  <DateTimePicker
    value={
      quizData.birthDate
        ? new Date(`${quizData.birthDate}T12:00:00`)
        : new Date()
    }
    mode="date"
    display="default"
    maximumDate={new Date()}
    onValueChange={(event, selectedDate) => {
      if (selectedDate) {
        const year = selectedDate.getFullYear();

        const month = String(
          selectedDate.getMonth() + 1,
        ).padStart(2, '0');

        const day = String(
          selectedDate.getDate(),
        ).padStart(2, '0');

        setQuizData(previous => ({
          ...previous,
          birthDate: `${year}-${month}-${day}`,
        }));
      }

      setShowDatePicker(false);
    }}
    onDismiss={() => {
      setShowDatePicker(false);
    }}
  />
) : null}
          </View>
        ) : null}

        {currentStep === 'GENDER' ? (
          <View style={styles.speciesQuestion}>
            <Text style={styles.speciesText}>
              {quizData.petName} یه
            </Text>

            <QuizSelect
              value={quizData.gender}
              options={[
                { label: 'دختر', value: 'FEMALE' },
                { label: 'پسر', value: 'MALE' },
                { label: 'مطمئن نیستم', value: 'UNKNOWN' },
              ]}
              onChange={value =>
                setQuizData(previous => ({
                  ...previous,
                  gender: value as 'MALE' | 'FEMALE' | 'UNKNOWN',
                }))
              }
            />

            <Text style={styles.speciesText}>هست.</Text>
          </View>
        ) : null}

        {currentStep === 'NEUTERED' ? (
          <View style={styles.neuteredQuestion}>
            <Text style={styles.genderTitle}>
              {quizData.petName} عقیم شده؟
            </Text>

            <QuizSelect
              value={
                quizData.neutered === null
                  ? null
                  : quizData.neutered === 'UNKNOWN'
                    ? 'UNKNOWN'
                    : quizData.neutered
                      ? 'YES'
                      : 'NO'
              }
              options={[
                { label: 'بله', value: 'YES' },
                { label: 'خیر', value: 'NO' },
                { label: 'مطمئن نیستم', value: 'UNKNOWN' },
              ]}
              onChange={value =>
                setQuizData(previous => ({
                  ...previous,
                  neutered:
                    value === 'YES'
                      ? true
                      : value === 'NO'
                        ? false
                        : 'UNKNOWN',
                }))
              }
            />
          </View>
        ) : null}

        {currentStep === 'WEIGHT' ? (
          <View style={styles.weightQuestion}>
            <Text style={styles.weightText}>
              {quizData.petName} حدود
            </Text>

            <TextInput
              style={styles.weightInput}
              value={
                quizData.weightKg !== null
                  ? String(quizData.weightKg)
                  : ''
              }
              onChangeText={value => {
                const normalizedValue = value.replace(',', '.');
                const parsedValue =
                  normalizedValue.trim() === ''
                    ? null
                    : Number(normalizedValue);

                setQuizData(previous => ({
                  ...previous,
                  weightKg:
                    parsedValue === null ||
                    !Number.isFinite(parsedValue)
                      ? parsedValue === null
                        ? null
                        : Number.NaN
                      : parsedValue,
                }));
              }}
              keyboardType="decimal-pad"
              placeholder="12.5"
            />

            <Text style={styles.weightText}>کیلو وزنشه.</Text>

            <Text style={styles.weightHint}>
              وزن دقیقش رو نمی‌دونی؟ نزدیک‌ترین عددی که حدس می‌زنی رو
              وارد کن.
            </Text>
          </View>
        ) : null}

        {currentStep === 'ACTIVITY' ? (
          <View style={styles.activityQuestion}>
            <Text style={styles.activityText}>
              {quizData.species === 'DOG'
                ? `${quizData.petName} روزانه چقدر`
                : `${quizData.petName} معمولاً چقدر`}
            </Text>

            <QuizSelect
              value={quizData.activityLevel}
              options={activityOptions}
              onChange={value =>
                setQuizData(previous => ({
                  ...previous,
                  activityLevel: value as
                    | 'LOW'
                    | 'MODERATE'
                    | 'HIGH'
                    | 'VERY_HIGH',
                }))
              }
            />

            <Text style={styles.activityText}>
              {quizData.species === 'DOG'
                ? 'فعالیت داره؟'
                : 'فعاله؟'}
            </Text>
          </View>
        ) : null}

        {currentStep === 'BODY_CONDITION' ? (
          <View style={styles.bodyConditionQuestion}>
            <Text style={styles.bodyConditionTitle}>
              به نظرت فرم بدن {quizData.petName} چطوره؟
            </Text>

            <View style={styles.bodyConditionOptions}>
              <BodyConditionOption
                label="لاغر"
                score={3}
                image={require('../../../assets/1.jpg')}
                selected={quizData.bodyConditionScore === 3}
                onPress={() =>
                  setQuizData(previous => ({
                    ...previous,
                    bodyConditionScore: 3,
                  }))
                }
              />

              <BodyConditionOption
                label="مناسب"
                score={5}
                image={require('../../../assets/2.jpg')}
                selected={quizData.bodyConditionScore === 5}
                onPress={() =>
                  setQuizData(previous => ({
                    ...previous,
                    bodyConditionScore: 5,
                  }))
                }
              />

              <BodyConditionOption
                label="کمی اضافه‌وزن"
                score={7}
                image={require('../../../assets/3.jpg')}
                selected={quizData.bodyConditionScore === 7}
                onPress={() =>
                  setQuizData(previous => ({
                    ...previous,
                    bodyConditionScore: 7,
                  }))
                }
              />
            </View>
          </View>
        ) : null}

        {currentStep === 'DIETARY_RESTRICTIONS' ? (
          <View style={styles.dietaryQuestion}>
            <Text style={styles.dietaryTitle}>
              {quizData.petName} به غذایی حساسیت داره؟
            </Text>

            {dietaryRestrictionOptions.map(option => (
              <DietaryRestrictionOption
                key={option.value}
                label={option.label}
                selected={quizData.dietaryRestrictions.includes(
                  option.value,
                )}
                onPress={() => {
                  setQuizData(previous => {
                    const current = previous.dietaryRestrictions;

                    const updated = current.includes(option.value)
                      ? current.filter(item => item !== option.value)
                      : [
                          ...current.filter(item => item !== 'NONE'),
                          option.value,
                        ];

                    return {
                      ...previous,
                      dietaryRestrictions: updated,
                    };
                  });
                }}
              />
            ))}

            <DietaryRestrictionOption
              label="نه، تا جایی که می‌دونم"
              selected={quizData.dietaryRestrictions.includes('NONE')}
              onPress={() =>
                setQuizData(previous => ({
                  ...previous,
                  dietaryRestrictions: ['NONE'],
                }))
              }
            />
          </View>
        ) : null}

        {error ? <Text style={styles.error}>{error}</Text> : null}

        <Button title="ادامه" onPress={goToNextStep} />
      </View>
    </SafeAreaView>
  );
}
