import { useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import { apiPost } from '../services/api/client';
import type { CreatePetRequestDto } from '../types/pet/CreatePetRequestDto';
import type { CreatePetResponseDto } from '../types/pet/CreatePetResponseDto';

export default function CreatePetScreen() {
  const [name, setName] = useState('');
  const [species, setSpecies] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  async function handleCreatePet() {
    if (!name.trim() || !species.trim()) {
      setMessage('Please enter pet name and species.');
      return;
    }

    setLoading(true);
    setMessage('');

    const request: CreatePetRequestDto = {
      name: name.trim(),
      species: species.trim(),
    };

    try {
      const response = await apiPost<
        CreatePetRequestDto,
        CreatePetResponseDto
      >('/pets', request);

      setMessage(
        `Pet created: ${response.data.name} (#${response.data.id})`,
      );

      setName('');
      setSpecies('');
    } catch {
      setMessage('Unable to create pet.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Create your pet</Text>

      <Text style={styles.label}>Name</Text>

      <TextInput
        style={styles.input}
        placeholder="e.g. Milo"
        value={name}
        onChangeText={setName}
        editable={!loading}
      />

      <Text style={styles.label}>Species</Text>

      <TextInput
        style={styles.input}
        placeholder="e.g. DOG"
        value={species}
        onChangeText={setSpecies}
        editable={!loading}
        autoCapitalize="characters"
      />

      <Pressable
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleCreatePet}
        disabled={loading}
      >
        {loading ? (
          <ActivityIndicator />
        ) : (
          <Text style={styles.buttonText}>Create Pet</Text>
        )}
      </Pressable>

      {message ? <Text style={styles.message}>{message}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    marginBottom: 32,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 12,
    marginBottom: 20,
    fontSize: 16,
  },
  button: {
    minHeight: 48,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#222',
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  message: {
    marginTop: 20,
    fontSize: 15,
  },
});