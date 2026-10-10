import { Pressable, StyleSheet, Text, View } from 'react-native';
import { useState } from 'react';

type QuizSelectOption = {
  label: string;
  value: string;
};

type QuizSelectProps = {
  value: string | null;
  options: QuizSelectOption[];
  onChange: (value: string) => void;
};

export function QuizSelect({
  value,
  options,
  onChange,
}: QuizSelectProps) {
  const selectedOption = options.find(
    (option) => option.value === value,
  );
  const [isOpen, setIsOpen] = useState(false);

  return (
    <View>
      <Pressable
  style={styles.select}
  onPress={() => setIsOpen((previous) => !previous)}
>
        <Text>
          {selectedOption?.label ?? '------'}
        </Text>

        <Text>⌄</Text>
      </Pressable>
      {isOpen ? (
  <View style={styles.options}>
    {options.map((option) => (
      <Pressable
        key={option.value}
        style={styles.option}
        onPress={() => {
          onChange(option.value);
          setIsOpen(false);
        }}
      >
        <Text style={styles.optionLabel}>
          {option.label}
        </Text>
      </Pressable>
    ))}
  </View>
) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  select: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },

  value: {
    fontSize: 28,
    textDecorationLine: 'underline',
  },

  options: {
    marginTop: 8,
    alignSelf: 'flex-start',
    minWidth: 120,
  },

  option: {
    paddingVertical: 10,
  },

  optionLabel: {
    fontSize: 22,
    textAlign: 'right',
  },
});