import { Pressable, StyleSheet, Text } from 'react-native';

type QuizChoiceProps = {
  label: string;
  selected: boolean;
  onPress: () => void;
};

export function QuizChoice({
  label,
  selected,
  onPress,
}: QuizChoiceProps) {
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
    paddingVertical: 16,
    paddingHorizontal: 20,
    borderWidth: 1,
    borderColor: '#999',
    borderRadius: 12,
    marginBottom: 12,
  },

  selected: {
    borderWidth: 2,
  },

  label: {
    fontSize: 20,
    textAlign: 'right',
  },
});