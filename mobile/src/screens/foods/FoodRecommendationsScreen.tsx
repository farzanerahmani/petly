
import { useCallback, useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Linking,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import { apiGet } from '../../services/api/client';
import { getAccessToken } from '../../services/auth/tokenStorage';

type PaymentMethod = {
  code: string;
  name: string;
  termsUrl: string | null;
  notes: string | null;
  verifiedAt: string | null;
};

type FoodPrice = {
  id: number;
  sellerName: string | null;
  priceAmount: number | null;
  currency: 'IRR' | 'IRT' | string | null;
  packageWeightGrams: number | null;
  productUrl: string | null;
  checkedAt: string | null;
  petShopId: number | null;
  petShopName: string | null;
  city: string | null;
  storeAddress: string | null;
  available: boolean | null;
  stockCheckedAt: string | null;
  lastVerifiedAt: string | null;
  paymentMethods: PaymentMethod[] | null;
};

type FoodProduct = {
  id: number;
  name: string;
  brand: string | null;
  species: string;
  foodType: string | null;
  lifeStage: string | null;
  description: string | null;
  ingredients: string | null;
  nutrition: {
    energyKcalPer100g: number | null;
    proteinPercent: number | null;
    fatPercent: number | null;
    fiberPercent: number | null;
    moisturePercent: number | null;
    source: string | null;
    verifiedAt: string | null;
  } | null;
  prices: FoodPrice[] | null;
};

type Recommendation = {
  food: FoodProduct;
  score: number;
  reasons: string[] | null;
  warnings: string[] | null;
};

type FoodRecommendationsResponse = {
  petId: number;
  petName: string;
  species: string;
  latestWeightKg: number | null;
  weightRecordedDate: string | null;
  bodyConditionScore: number | null;
  bodyConditionRecordedDate: string | null;
  expectedLifeStage: string | null;
  notice: string | null;
  recommendations: Recommendation[] | null;
};

type Props = {
  petId: number;
  onBack: () => void;
};

function displayDate(value: string | null | undefined): string {
  if (!value) return 'نامشخص';

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return 'نامشخص';

  return date.toLocaleDateString('fa-IR');
}

function displayNumber(value: number | null | undefined): string {
  if (value === null || value === undefined) return 'نامشخص';

  return new Intl.NumberFormat('fa-IR', {
    maximumFractionDigits: 2,
  }).format(value);
}

function displayPrice(price: FoodPrice): string {
  if (price.priceAmount === null || price.priceAmount === undefined) {
    return 'قیمت ثبت نشده';
  }

  const amount = displayNumber(price.priceAmount);
  const currency =
    price.currency === 'IRR'
      ? 'ریال'
      : price.currency === 'IRT'
        ? 'تومان'
        : price.currency ?? '';

  return `${amount} ${currency}`.trim();
}

function displayStock(price: FoodPrice): string {
  if (price.available === true) {
    return `طبق آخرین اطلاعات فروشگاه: موجود (بررسی موجودی: ${displayDate(price.stockCheckedAt)})`;
  }

  if (price.available === false) {
    return `طبق آخرین اطلاعات فروشگاه: ناموجود (بررسی موجودی: ${displayDate(price.stockCheckedAt)})`;
  }

  return 'وضعیت موجودی مشخص نیست';
}

export function FoodRecommendationsScreen({ petId, onBack }: Props) {
  const [data, setData] = useState<FoodRecommendationsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reloadKey, setReloadKey] = useState(0);

  const loadRecommendations = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const token = await getAccessToken();
      if (!token) {
        throw new Error('توکن ورود پیدا نشد؛ لطفاً دوباره وارد شو.');
      }

     
const result = await apiGet<FoodRecommendationsResponse>(
  `/pets/${petId}/food-recommendations`,
  token,
);

console.log(
  'Parsed food recommendations:',
  JSON.stringify(result, null, 2),
);

if (!result || !Array.isArray(result.recommendations)) {
  console.error('Unexpected food recommendation response:', result);
  throw new Error('ساختار پاسخ پیشنهاد غذا با فرمت مورد انتظار مطابقت ندارد.');
}

setData(result);

    } catch (err) {
      setData(null);
      setError(
        err instanceof Error
          ? err.message
          : 'دریافت پیشنهادهای غذا ناموفق بود.',
      );
    } finally {
      setLoading(false);
    }
  }, [petId]);

  useEffect(() => {
    void loadRecommendations();
  }, [loadRecommendations, reloadKey]);

  const openLink = async (url: string | null | undefined) => {
    if (!url) return;

    try {
      await Linking.openURL(url);
    } catch {
      setError('باز کردن لینک ممکن نشد.');
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Pressable onPress={onBack} style={styles.backButton}>
          <Text style={styles.backText}>بازگشت</Text>
        </Pressable>
        <Text style={styles.title}>غذاهای پیشنهادی</Text>
      </View>

      {loading ? (
        <View style={styles.center}>
          <ActivityIndicator size="large" color="#237A4B" />
          <Text style={styles.message}>در حال دریافت پیشنهادها...</Text>
        </View>
      ) : error ? (
        <View style={styles.center}>
          <Text style={styles.error}>{error}</Text>
          <Pressable
            style={styles.primaryButton}
            onPress={() => setReloadKey(value => value + 1)}
          >
            <Text style={styles.primaryButtonText}>تلاش دوباره</Text>
          </Pressable>
        </View>
      ) : (
        <ScrollView contentContainerStyle={styles.content}>
          <Text style={styles.subtitle}>
            پیشنهادها برای {data?.petName ?? 'پت شما'}
          </Text>

          {data?.notice ? (
            <Text style={styles.notice}>{data.notice}</Text>
          ) : null}

          {!data?.recommendations?.length ? (
            <View style={styles.card}>
              <Text style={styles.message}>
                فعلاً پیشنهادی برای نمایش وجود ندارد.
              </Text>
            </View>
          ) : (
            data.recommendations.map((item, index) => {
              const food = item.food;

              return (
                <View key={food.id} style={styles.card}>
                  <Text style={styles.foodName}>
                    {index + 1}. {food.name}
                  </Text>

                  {food.brand ? (
                    <Text style={styles.detail}>برند: {food.brand}</Text>
                  ) : null}

                  <Text style={styles.detail}>
                    نوع غذا: {food.foodType ?? 'نامشخص'}
                  </Text>
                  <Text style={styles.detail}>
                    مرحله زندگی: {food.lifeStage ?? 'نامشخص'}
                  </Text>

                  {food.description ? (
                    <Text style={styles.detail}>{food.description}</Text>
                  ) : null}

                  {food.nutrition ? (
                    <View style={styles.section}>
                      <Text style={styles.sectionTitle}>ارزش غذایی</Text>
                      <Text style={styles.detail}>
                        انرژی: {displayNumber(food.nutrition.energyKcalPer100g)} کیلوکالری در ۱۰۰ گرم
                      </Text>
                      <Text style={styles.detail}>
                        پروتئین: {displayNumber(food.nutrition.proteinPercent)}٪
                      </Text>
                      <Text style={styles.detail}>
                        چربی: {displayNumber(food.nutrition.fatPercent)}٪
                      </Text>
                      <Text style={styles.detail}>
                        فیبر: {displayNumber(food.nutrition.fiberPercent)}٪
                      </Text>
                      <Text style={styles.detail}>
                        رطوبت: {displayNumber(food.nutrition.moisturePercent)}٪
                      </Text>
                      <Text style={styles.muted}>
                        منبع اطلاعات: {food.nutrition.source ?? 'ثبت نشده'}
                      </Text>
                    </View>
                  ) : (
                    <Text style={styles.muted}>
                      اطلاعات ارزش غذایی ثبت نشده است.
                    </Text>
                  )}

                  {item.reasons?.length ? (
                    <View style={styles.section}>
                      <Text style={styles.sectionTitle}>دلایل پیشنهاد</Text>
                      {item.reasons.map((reason, i) => (
                        <Text key={`${food.id}-reason-${i}`} style={styles.detail}>
                          • {reason}
                        </Text>
                      ))}
                    </View>
                  ) : null}

                  {item.warnings?.length ? (
                    <View style={styles.warningBox}>
                      <Text style={styles.sectionTitle}>نکات قابل توجه</Text>
                      {item.warnings.map((warning, i) => (
                        <Text key={`${food.id}-warning-${i}`} style={styles.detail}>
                          • {warning}
                        </Text>
                      ))}
                    </View>
                  ) : null}

                  <View style={styles.section}>
                    <Text style={styles.sectionTitle}>فروشگاه‌ها و قیمت‌ها</Text>

                    {!food.prices?.length ? (
                      <Text style={styles.muted}>
                        اطلاعات قیمت و فروشگاه موجود نیست.
                      </Text>
                    ) : (
                      food.prices.map(price => (
                        <View key={price.id} style={styles.offer}>
                          <Text style={styles.offerName}>
                            {price.petShopName ?? price.sellerName ?? 'فروشگاه ثبت‌نشده'}
                          </Text>

                          <Text style={styles.price}>{displayPrice(price)}</Text>

                          {price.packageWeightGrams ? (
                            <Text style={styles.detail}>
                              وزن بسته: {displayNumber(price.packageWeightGrams)} گرم
                            </Text>
                          ) : null}

                          {price.city ? (
                            <Text style={styles.detail}>شهر: {price.city}</Text>
                          ) : null}

                          {price.storeAddress ? (
                            <Text style={styles.detail}>
                              نشانی: {price.storeAddress}
                            </Text>
                          ) : null}

                          <Text style={styles.detail}>{displayStock(price)}</Text>

                          <Text style={styles.muted}>
                            زمان ثبت قیمت: {displayDate(price.checkedAt)}
                          </Text>

                          {price.lastVerifiedAt ? (
                            <Text style={styles.muted}>
                              آخرین تأیید اطلاعات: {displayDate(price.lastVerifiedAt)}
                            </Text>
                          ) : null}

                          {price.paymentMethods?.length ? (
                            <View style={styles.paymentBox}>
                              <Text style={styles.sectionTitle}>
                                روش‌های پرداخت ثبت‌شده و تأییدشده
                              </Text>
                              {price.paymentMethods.map(method => (
                                <View key={method.code}>
                                  <Text style={styles.detail}>• {method.name}</Text>
                                  {method.notes ? (
                                    <Text style={styles.muted}>{method.notes}</Text>
                                  ) : null}
                                  {method.termsUrl ? (
                                    <Pressable onPress={() => openLink(method.termsUrl)}>
                                      <Text style={styles.link}>مشاهده شرایط پرداخت</Text>
                                    </Pressable>
                                  ) : null}
                                </View>
                              ))}
                            </View>
                          ) : (
                            <Text style={styles.muted}>
                              روش پرداخت تأییدشده‌ای برای این فروشگاه ثبت نشده است.
                            </Text>
                          )}

                          {price.productUrl ? (
                            <Pressable
                              style={styles.secondaryButton}
                              onPress={() => openLink(price.productUrl)}
                            >
                              <Text style={styles.secondaryButtonText}>
                                مشاهده محصول در فروشگاه
                              </Text>
                            </Pressable>
                          ) : null}
                        </View>
                      ))
                    )}
                  </View>
                </View>
              );
            })
          )}

          <Text style={styles.footer}>
            پیشنهادها جایگزین نظر دامپزشک نیستند. قیمت و موجودی را پیش از خرید در فروشگاه بررسی کن.
          </Text>

          <Pressable
            style={styles.primaryButton}
            onPress={() => setReloadKey(value => value + 1)}
          >
            <Text style={styles.primaryButtonText}>به‌روزرسانی پیشنهادها</Text>
          </Pressable>
        </ScrollView>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    direction: 'rtl',
  },
  header: {
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 12,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  backButton: {
    paddingVertical: 8,
    paddingHorizontal: 12,
  },
  backText: {
    color: '#237A4B',
    fontSize: 15,
    fontWeight: '600',
  },
  title: {
    fontSize: 23,
    fontWeight: '700',
    color: '#17221B',
  },
  content: {
    padding: 20,
    paddingBottom: 36,
  },
  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  message: {
    marginTop: 12,
    fontSize: 16,
    lineHeight: 25,
    color: '#34443A',
    textAlign: 'center',
  },
  error: {
    fontSize: 16,
    lineHeight: 26,
    color: '#B42318',
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#34443A',
    marginBottom: 12,
    textAlign: 'right',
  },
  notice: {
    fontSize: 14,
    lineHeight: 23,
    color: '#68736C',
    marginBottom: 16,
    textAlign: 'right',
  },
  card: {
    padding: 16,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: '#D7E9DC',
    borderRadius: 16,
    backgroundColor: '#F8FBF8',
  },
  foodName: {
    fontSize: 19,
    fontWeight: '700',
    color: '#237A4B',
    marginBottom: 10,
    textAlign: 'right',
  },
  detail: {
    fontSize: 14,
    lineHeight: 23,
    color: '#34443A',
    marginTop: 4,
    textAlign: 'right',
  },
  muted: {
    fontSize: 12,
    lineHeight: 20,
    color: '#68736C',
    marginTop: 6,
    textAlign: 'right',
  },
  section: {
    marginTop: 16,
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: '700',
    color: '#237A4B',
    marginBottom: 6,
    textAlign: 'right',
  },
  warningBox: {
    marginTop: 14,
    padding: 12,
    borderRadius: 10,
    backgroundColor: '#FFF6E5',
  },
  offer: {
    marginTop: 10,
    padding: 12,
    borderRadius: 12,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E2E8E3',
  },
  offerName: {
    fontSize: 15,
    fontWeight: '600',
    color: '#17221B',
    textAlign: 'right',
  },
  price: {
    fontSize: 18,
    fontWeight: '700',
    color: '#237A4B',
    marginTop: 6,
    textAlign: 'right',
  },
  paymentBox: {
    marginTop: 12,
    padding: 10,
    borderRadius: 10,
    backgroundColor: '#F0F7F2',
  },
  link: {
    marginTop: 6,
    color: '#237A4B',
    textDecorationLine: 'underline',
    textAlign: 'right',
  },
  primaryButton: {
    marginTop: 20,
    paddingVertical: 14,
    paddingHorizontal: 20,
    borderRadius: 12,
    backgroundColor: '#237A4B',
    alignItems: 'center',
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
  secondaryButton: {
    marginTop: 12,
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#237A4B',
    alignItems: 'center',
  },
  secondaryButtonText: {
    color: '#237A4B',
    fontSize: 14,
    fontWeight: '600',
  },
  footer: {
    marginTop: 16,
    fontSize: 12,
    lineHeight: 21,
    color: '#68736C',
    textAlign: 'right',
  },
});
