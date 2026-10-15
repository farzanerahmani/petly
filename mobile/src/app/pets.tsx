import { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { router } from 'expo-router';
import { apiGet } from '../services/api/client';
import { getAccessToken } from '../services/auth/tokenStorage';
import type { PetResponseDto } from '../types/pet/PetResponseDto';

export default function PetsScreen() {
  const [pets, setPets] = useState<PetResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function loadPets() {
      try {
        const token = await getAccessToken();

        if (!token) {
          router.replace('/login');
          return;
        }

        const result = await apiGet<PetResponseDto[]>('/pets', token);
        setPets(result);
      } catch (e) {
        setError(
          e instanceof Error ? e.message : 'دریافت فهرست حیوان‌ها ناموفق بود.',
        );
      } finally {
        setLoading(false);
      }
    }

    void loadPets();
  }, []);

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" color="#237A4B" />
        <Text style={styles.message}>در حال دریافت حیوان‌ها...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>حیوان‌های من</Text>

      {error ? (
        <Text style={styles.message}>{error}</Text>
      ) : pets.length === 0 ? (
        <Text style={styles.message}>هنوز حیوانی ثبت نشده است.</Text>
      ) : (
        pets.map((pet) => (
          <View key={pet.id} style={styles.petCard}>
            <Text style={styles.petName}>{pet.name}</Text>
            <Text style={styles.petDetails}>
              {pet.species === 'DOG' ? 'سگ' : pet.species === 'CAT' ? 'گربه' : pet.species}
            </Text>
          </View>
        ))
      )}

      <Pressable
        style={styles.button}
        onPress={() => router.push('/onboarding')}
      >
        <Text style={styles.buttonText}>افزودن حیوان</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    padding: 24,
    paddingTop: 64,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    padding: 24,
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    color: '#17221B',
    textAlign: 'right',
    marginBottom: 24,
  },
  message: {
    fontSize: 16,
    color: '#68736C',
    textAlign: 'right',
    marginTop: 12,
  },
  petCard: {
    borderWidth: 1,
    borderColor: '#D7DED9',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
  },
  petName: {
    fontSize: 18,
    fontWeight: '600',
    color: '#17221B',
    textAlign: 'right',
  },
  petDetails: {
    fontSize: 14,
    color: '#68736C',
    textAlign: 'right',
    marginTop: 6,
  },
  button: {
    minHeight: 52,
    backgroundColor: '#237A4B',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 16,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
});
