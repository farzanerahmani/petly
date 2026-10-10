import { Pressable, StyleSheet, Text } from 'react-native';

type QuizOptionProps = {
  label: string;
  selected: boolean;
  onPress: () => void;
};

export function QuizOption({
  label,
  selected,
  onPress,
}: QuizOptionProps) {
  return (
    <Pressable
      style={[
        styles.option,
        selected && styles.selected,
      ]}
      onPress={onPress}
    >
      <Text style={styles.label}>
        {label}
      </Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  option: {
    padding: 16,
    borderWidth: 1,
    borderColor: '#999',
    borderRadius: 8,
    marginBottom: 12,
  },
  selected: {
    borderWidth: 2,
  },
  label: {
    fontSize: 18,
  },
});