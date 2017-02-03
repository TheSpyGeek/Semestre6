

for i in LesAchats_*
do
	java EssaiJoin Test/$i Test/LesVins Res/stats_naif.txt Res/stats_hash.txt >> stats.txt
done