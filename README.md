# InfinityFragementPagerAdapter
This is endless adapter. It can be two type:
- Cyclic. It create pages by cycle. if count 3 than cycle is like 0-1-2-0-1-2-0-1-2...
- Infinity shift. Create page by shift at start position. It can be like (-3)-(-2)-(-1)-0(start)-1-2-3

# Usage
ViewPager delegate some funcions to adapter.
- setCurrentItem;
- getCurrentItem;
- setOnPageChangeListener;
