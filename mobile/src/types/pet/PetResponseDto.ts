export type PetResponseDto = {
  id: number;
  name: string;
  species: 'DOG' | 'CAT';
  breed: string | null;
  gender: 'MALE' | 'FEMALE' | 'UNKNOWN' | null;
  birthDate: string | null;
  activityLevel: 'LOW' | 'MODERATE' | 'HIGH' | 'VERY_HIGH' | null;
    foodTypePreference: 'DRY' | 'WET' | 'BOTH' | 'NO_PREFERENCE';
currentFood: string;
currentFoodSatisfaction: 'YES' | 'PARTLY' | 'NO' | 'UNKNOWN';
  createdAt: string;
  updatedAt: string;

};
