export type QuizData = {
  parentFirstName: string;
  phoneNumber: string;

  petName: string;
  species: 'DOG' | 'CAT' | null;
  breed: string | null;

  birthDate: string | null;
  gender: 'MALE' | 'FEMALE' | 'UNKNOWN' | null;
  neutered: boolean | 'UNKNOWN' | null;

  weightKg: number | null;

  activityLevel: 'LOW' | 'MODERATE' | 'HIGH' | 'VERY_HIGH' | null;

  bodyConditionScore: number | null;

  dietaryRestrictions: string[];
};