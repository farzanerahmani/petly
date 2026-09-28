import { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { apiGet } from '../services/api/client';
import type { HealthResponse } from '../types/HealthResponse';

export default function HomeScreen() {
  const [status, setStatus] = useState('Checking...');
  const [error, setError] = useState(false);

  useEffect(() => {
    apiGet<HealthResponse>('/health')
       .then((response) => {
         setStatus(response.data.status);
        })
      .catch(() => {
          setError(true);
           setStatus(
            'Unable to connect');
      });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Petly</Text>
      <Text>
        Backend status: {error ? 'ERROR' : status}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    marginBottom: 12,
  },
});