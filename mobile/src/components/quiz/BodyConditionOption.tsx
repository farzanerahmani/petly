import { Image, Pressable, StyleSheet, Text } from 'react-native';


type BodyConditionOptionProps = {
  label: string;
  score: number;
  image: any;
  selected: boolean;
  onPress: () => void;
};

export function BodyConditionOption({
  label,
  score,
  image,
  selected,
  onPress,
}: BodyConditionOptionProps) {
  return (
    <Pressable
      style={[
        styles.option,
        selected && styles.selected,
      ]}
      onPress={onPress}
    >
      <Image
  source={image}
  style={styles.image}
/>

      <Text style={styles.label}>
        {label}
      </Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  option: {
    width: '48%',
    marginBottom: 16,
    borderWidth: 1,
    borderColor: '#999',
    borderRadius: 12,
    padding: 8,
  },

  selected: {
    borderWidth: 2,
  },

  imagePlaceholder: {
    height: 120,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#eee',
    borderRadius: 8,
    marginBottom: 8,
  },

  placeholderText: {
    fontSize: 16,
  },

  label: {
    fontSize: 18,
    textAlign: 'center',
  },
  image: {
  width: '100%',
  height: 120,
  resizeMode: 'contain',
},
});