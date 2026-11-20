import os
BASE = os.path.dirname(os.path.abspath(__file__))
RES = os.path.join(BASE, 'results')
os.makedirs(RES, exist_ok=True)
P = lambda f: os.path.join(RES, f)
import pandas as pd, numpy as np
df = pd.read_csv(P('results.csv'))
g = df.groupby(['type','N','cutoff'])['ms']
s = g.agg(median='median', mean='mean', std='std', n='count').reset_index()
s['rel'] = s.groupby(['type','N'])['median'].transform(lambda x: x/x.min())
s.to_csv(P('summary.csv'), index=False)
pd.set_option('display.width',250)
for t in ['random','sorted','reverse','nearly_sorted','few_distinct']:
    print('==',t)
    for N in [10000,100000,1000000]:
        d = s[(s.type==t)&(s.N==N)].set_index('cutoff')
        best = d['median'].idxmin()
        near = d[d.rel<=1.03].index.tolist()
        print(N,'best',best,'t=%.3f'%d.loc[best,'median'],'within3%:',near,' c=1: %.2f  c=7: %.2f c=10: %.2f'%(d.loc[1,'rel'],d.loc[7,'rel'],d.loc[10,'rel']))
p = s.pivot_table(index='cutoff',columns=['type','N'],values='rel')
print(p[('random',1000000)].round(3).to_string())
# overall mean rel
print(s.groupby('cutoff').rel.mean().round(3).to_string())
