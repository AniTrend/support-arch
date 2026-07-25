# ui/src/main/kotlin/co/anitrend/arch/ui/pager/

## Responsibility

Legacy ViewPager adapter base for fragment pages with title resources.

## Design Patterns

Abstract `FragmentStatePagerAdapter` template with a mutable title list loaded from a string array resource.

## Data & Control Flow

Consumers set titles from resources, `getCount` returns title count, subclasses provide fragments, and `getPageTitle` returns uppercase titles or an empty string for invalid positions.

## Integration Points

Uses AndroidX FragmentStatePagerAdapter and extension string resource helpers. Intended for ViewPager based screens.
