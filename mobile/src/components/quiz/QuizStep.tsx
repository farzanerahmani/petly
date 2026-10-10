import { StyleSheet, Text, TextInput, View } from 'react-native';

type QuizStepProps = {
  question: string;
  value?: string;
  onChange?: (value: string) => void;
  showTextInput?: boolean;
};

export function QuizStep({
  question,
  value = '',
  onChange,
  showTextInput = true,
}: QuizStepProps) {
  return (
    <View style={styles.container}>
      <Text style={styles.question}>
        {question}
      </Text>

      {showTextInput ? (
        <TextInput
          style={styles.input}
          value={value}
          onChangeText={onChange}
          placeholder="اینجا وارد کن"
        />
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 32,
  },
  question: {
    fontSize: 28,
    marginBottom: 24,
  },
  input: {
    height: 52,
    borderWidth: 1,
    borderColor: '#999',
    borderRadius: 8,
    paddingHorizontal: 16,
    fontSize: 18,
  },
});