import { apiPostAuthenticated } from '../api/client';
import {
  calculateDailyCalories,
  type ActivityLevel,
  type CalorieEstimate,
  type NeuteredStatus,
  type PetSpecies,
} from './calorieCalculator';

type SavePetCalorieRequest = {
  petId: number;
  weightKg: number;
  birthDate: string;
  neutered: NeuteredStatus;
  activityLevel: ActivityLevel;
  species: PetSpecies;
};

type SavePetCalorieResponse = {
  id: number;
  petId: number;
  weightKg: number;
  ageMonths: number;
  rerKcal: number;
  factor: number;
  merKcal: number;
  calculatedAt: string;
};

export async function calculateAndSavePetCalories(
  input: SavePetCalorieRequest,
): Promise<{
  estimate: CalorieEstimate;
  savedRecord: SavePetCalorieResponse;
}> {
  const estimate = calculateDailyCalories({
    species: input.species,
    weightKg: input.weightKg,
    birthDate: input.birthDate,
    neutered: input.neutered,
    activityLevel: input.activityLevel,
  });

  const savedRecord = await apiPostAuthenticated<
    SavePetCalorieRequest,
    SavePetCalorieResponse
  >('/pets/calorie-records', input);

  return { estimate, savedRecord };
}