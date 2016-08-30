update armour_skills set num_slots = 2 where armour_name='Grand Mutsu Eboshi/Grand Amagi Jingasa';
update armour_skills set num_slots = 1 where armour_name='Grand Mutsu Doate/Grand Amagi Muneate';
update armour_skills set num_slots = 2 where armour_name='Grand Mutsu Kote/Grand Amagi Kote';
update armour_skills set num_slots = 0 where armour_name='Grand Mutsu Koshiate/Grand Amagi Koshiate';
update armour_skills set num_slots = 3 where armour_name='Grand Mutsu Gosuku/Grand Amagi Hakama';

update armour_skills set num_slots = 3 where armour_name='Grand Yamato Kabuto/Grand Hyuga Hachigane';
update armour_skills set num_slots = 1 where armour_name='Grand Yamato Doate/Grand Hyuga Muneate';
update armour_skills set num_slots = 2 where armour_name='Grand Yamato Kote/Grand Hyuga Kote';
update armour_skills set num_slots = 0 where armour_name='Grand Yamato Koshiate/Grand Hyuga Koshiate';
update armour_skills set num_slots = 3 where armour_name='Grand Yamato Gusoku/Grand Hyuga Hakama';

update armour_skills set skill_value=2 where skill_name='Fencing' and armour_name='Grand Yamato Doate/Grand Hyuga Muneate';
update armour_skills set skill_value=3 where skill_name='Fencing' and armour_name='Grand Yamato Koshiate/Grand Hyuga Koshiate';