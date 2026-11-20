import os
BASE = os.path.dirname(os.path.abspath(__file__))
RES = os.path.join(BASE, 'results')
os.makedirs(RES, exist_ok=True)
P = lambda f: os.path.join(RES, f)
import pandas as pd, numpy as np, json
import matplotlib; matplotlib.use('Agg')
import matplotlib.pyplot as plt
s = pd.read_csv(P('summary.csv')); df = pd.read_csv(P('results.csv'))
names = {'random':'Ngẫu nhiên','sorted':'Đã sắp xếp','reverse':'Ngược','nearly_sorted':'Gần như đã sắp xếp','few_distinct':'Ít giá trị phân biệt (10)'}
Ns = {10000:'N = 10⁴',100000:'N = 10⁵',1000000:'N = 10⁶'}
def style(ax):
    ax.axvline(7,color='gray',ls=':',lw=1); ax.axvline(10,color='gray',ls='--',lw=1)
    ax.set_xscale('log'); ax.set_xticks([1,2,3,5,7,10,16,25,40,64,100]); ax.set_xticklabels([1,2,3,5,7,10,16,25,40,64,100])
    ax.grid(alpha=.3); ax.set_xlabel('CUTOFF (log)'); 
# fig1 random 3 sizes
fig,ax=plt.subplots(figsize=(7,4))
for N,l in Ns.items():
    d=s[(s.type=='random')&(s.N==N)]; ax.plot(d.cutoff,d.rel,'o-',ms=3,label=l)
style(ax); ax.set_ylabel('Thời gian / thời gian tốt nhất'); ax.set_title('Dữ liệu ngẫu nhiên: ảnh hưởng của CUTOFF'); ax.legend(); ax.set_ylim(0.95,1.5)
ax.text(7,1.45,'7',ha='right',color='gray'); ax.text(10,1.45,'10',ha='left',color='gray')
fig.tight_layout(); fig.savefig(P('fig1_random.png'),dpi=160); plt.close()
# fig2 N=1e6 all types
fig,ax=plt.subplots(figsize=(7,4))
for t,l in names.items():
    d=s[(s.type==t)&(s.N==1000000)]; ax.plot(d.cutoff,d.rel,'o-',ms=3,label=l)
style(ax); ax.set_ylabel('Thời gian / thời gian tốt nhất'); ax.set_title('N = 10⁶: so sánh các loại dữ liệu'); ax.legend(fontsize=8); ax.set_ylim(0.95,2.0)
fig.tight_layout(); fig.savefig(P('fig2_types.png'),dpi=160); plt.close()
# fig3 absolute with spread, random 1e6 and 1e5
fig,axs=plt.subplots(1,2,figsize=(9,3.6))
for ax,N in zip(axs,[100000,1000000]):
    x=df[(df.type=='random')&(df.N==N)]
    gq=x.groupby('cutoff').ms
    q25,q50,q75=gq.quantile(.25),gq.quantile(.5),gq.quantile(.75)
    ax.plot(q50.index,q50.values,'o-',ms=3); ax.fill_between(q50.index,q25.values,q75.values,alpha=.25)
    style(ax); ax.set_ylabel('ms'); ax.set_title(Ns[N]+' (median, vùng Q1–Q3)')
fig.tight_layout(); fig.savefig(P('fig3_abs.png'),dpi=160); plt.close()
# table data
rows=[]
for t,l in names.items():
    for N in Ns:
        d=s[(s.type==t)&(s.N==N)].set_index('cutoff'); b=d['median'].idxmin()
        rows.append([l,f'{N:,}',int(b),f"{d.loc[b,'median']:.2f}",f"{d.loc[1,'rel']:.2f}",f"{d.loc[7,'rel']:.2f}",f"{d.loc[10,'rel']:.2f}",f"{d.loc[16,'rel']:.2f}",f"{d.loc[25,'rel']:.2f}"])
with open(P('table.json'),'w',encoding='utf-8') as f: json.dump(rows,f,ensure_ascii=False)
r=s[s.type=='random']; print(r.groupby('cutoff').rel.mean().round(3).to_string())
x=df[(df.type=='random')&(df.N==1000000)]; print('CV%', (x.groupby('cutoff').ms.std()/x.groupby('cutoff').ms.mean()*100).describe().round(2).to_dict())
