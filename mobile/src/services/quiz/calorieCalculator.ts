export type PetSpecies = 'DOG' | 'CAT';
export type ActivityLevel = 'LOW' | 'MODERATE' | 'HIGH' | 'VERY_HIGH';
export type NeuteredStatus = boolean | 'UNKNOWN';

export type CalorieEstimate = {
  rerKcal: number;
  minKcal: number;
  maxKcal: number;
  isRange: boolean;
  factorMin: number;
  factorMax: number;
};

function getAgeMonths(birthDate: string, today = new Date()): number {
  // Parse YYYY-MM-DD as a local calendar date.
  const [year, month, day] = birthDate.split('-').map(Number);

  if (!year || !month || !day) {
    throw new Error('Invalid birth date');
  }

  let ageMonths =
    (today.getFullYear() - year) * 12 +
    (today.getMonth() + 1 - month);

  if (today.getDate() < day) {
    ageMonths -= 1;
  }

  return Math.max(0, ageMonths);
}

function getAdultFactor(
  species: PetSpecies,
  neutered: boolean,
  activityLevel: ActivityLevel,
): number {
  const factors =
    species === 'DOG'
      ? neutered
        ? [1.4, 1.5, 1.6]
        : [1.6, 1.7, 1.8]
      : neutered
        ? [1.2, 1.3, 1.4]
        : [1.4, 1.5, 1.6];

  // Product heuristic: activity chooses a position within the
  // published adult factor range; it is not a validated clinical rule.
  const index =
    activityLevel === 'LOW'
      ? 0
      : activityLevel === 'MODERATE'
        ? 1
        : 2;

  return factors[index];
}

export function calculateDailyCalories(input: {
  species: PetSpecies;
  weightKg: number;
  birthDate: string;
  neutered: NeuteredStatus;
  activityLevel: ActivityLevel;
}): CalorieEstimate {
  const { species, weightKg, birthDate, neutered, activityLevel } = input;

  if (!Number.isFinite(weightKg) || weightKg <= 0) {
    throw new Error('Weight must be greater than zero');
  }

  const ageMonths = getAgeMonths(birthDate);

  const rer = 70 * Math.pow(weightKg, 0.75);

  // Growth factors from the general AAHA guidance.
  if (ageMonths < 12) {
    const factor =
      species === 'DOG'
        ? ageMonths < 4
          ? 3.0
          : 2.0
        : 2.5;

    const calories = Math.round(rer * factor);

    return {
      rerKcal: Math.round(rer),
      minKcal: calories,
      maxKcal: calories,
      isRange: false,
      factorMin: factor,
      factorMax: factor,
    };
  }

  if (neutered !== 'UNKNOWN') {
    const factor = getAdultFactor(species, neutered, activityLevel);
    const calories = Math.round(rer * factor);

    return {
      rerKcal: Math.round(rer),
      minKcal: calories,
      maxKcal: calories,
      isRange: false,
      factorMin: factor,
      factorMax: factor,
    };
  }

  // Unknown neuter status: preserve both possibilities.
  const neuteredFactor = getAdultFactor(species, true, activityLevel);
  const intactFactor = getAdultFactor(species, false, activityLevel);

  const factorMin = Math.min(neuteredFactor, intactFactor);
  const factorMax = Math.max(neuteredFactor, intactFactor);

  return {
    rerKcal: Math.round(rer),
    minKcal: Math.round(rer * factorMin),
    maxKcal: Math.round(rer * factorMax),
    isRange: true,
    factorMin,
    factorMax,
  };
}
