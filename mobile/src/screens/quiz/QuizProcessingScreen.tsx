import { useEffect, useRef, useState } from 'react';
import {
  Animated,
  StyleSheet,
  Text,
  View,
} from 'react-native';

type QuizProcessingScreenProps = {
  petName: string;
  onComplete: () => void;
};

export function QuizProcessingScreen({
  petName,
  onComplete,
}: QuizProcessingScreenProps) {
    const steps = [
  'اطلاعات پایه',
  'وزن و وضعیت بدن',
  'میزان فعالیت',
  'حساسیت‌های غذایی',
];

const [currentStep, setCurrentStep] = useState(0);
const [completedSteps, setCompletedSteps] = useState<string[]>([]);

const opacity = useRef(new Animated.Value(0)).current;
useEffect(() => {
  opacity.setValue(0);

  Animated.timing(opacity, {
    toValue: 1,
    duration: 500,
    useNativeDriver: true,
  }).start();

  const timer = setTimeout(() => {
    setCompletedSteps(previous => [...previous, steps[currentStep]]);

if (currentStep < steps.length - 1) {
  setCurrentStep(previous => previous + 1);
} else {
  onComplete();
}
  }, 1000);

  return () => clearTimeout(timer);
}, [currentStep]);
  return (
 <View style={styles.container}>
  <Text style={styles.title}>
    داریم {petName} رو می‌شناسیم... 🐾
  </Text>

  {completedSteps.map(step => (
    <Text key={step} style={styles.completedStep}>
      ✓ {step}
    </Text>
  ))}

  <Animated.View style={{ opacity }}>
    <Text style={styles.step}>
      {steps[currentStep]}
    </Text>
  </Animated.View>
</View>
);
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 24,
    direction: 'rtl',
  },
  title: {
    fontSize: 30,
    lineHeight: 42,
    textAlign: 'center',
    marginBottom: 32,
  },
  step: {
    fontSize: 18,
    textAlign: 'right',
    marginBottom: 16,
  },
  completedStep: {
  fontSize: 18,
  textAlign: 'right',
  marginBottom: 16,
},
});