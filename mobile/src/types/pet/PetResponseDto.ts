export type PetResponseDto = {
  id: number;
  name: string;
  species: 'DOG' | 'CAT';
  breed: string | null;
  gender: 'MALE' | 'FEMALE' | 'UNKNOWN' | null;
  birthDate: string | null;
  activityLevel: 'LOW' | 'MODERATE' | 'HIGH' | 'VERY_HIGH' | null;
  createdAt: string;
  updatedAt: string;
};
