import { Pressable, StyleSheet, Text } from 'react-native';

type DietaryRestrictionOptionProps = {
  label: string;
  selected: boolean;
  onPress: () => void;
};

export function DietaryRestrictionOption({
  label,
  selected,
  onPress,
}: DietaryRestrictionOptionProps) {
  return (
    <Pressable
      style={[styles.option, selected && styles.selected]}
      onPress={onPress}
    >
      <Text style={styles.label}>{label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  option: {
    borderWidth: 1,
    borderColor: '#999',
    borderRadius: 12,
    paddingVertical: 14,
    paddingHorizontal: 18,
    marginBottom: 10,
  },
  selected: {
    borderWidth: 2,
  },
  label: {
    fontSize: 18,
    textAlign: 'right',
  },
});