package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class CursedGlyph extends PCLRelic implements OnEquipUnnamedReignRelicListener
{
    public static final String ID = CreateFullID(CursedGlyph.class);
    public static final int MAX_HP_LOSS = 4;

    public CursedGlyph()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, MAX_HP_LOSS);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            IncreaseMaxHP(m);
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onSpawnMonster(AbstractMonster monster)
    {
        super.onSpawnMonster(monster);

        IncreaseMaxHP(monster);

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {
        player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    protected void IncreaseMaxHP(AbstractMonster monster)
    {
        PCLActions.Bottom.Callback(new RelicAboveCreatureAction(monster, this), monster, (m, __) ->
        {
            int bonusHealth = 6;
            if (GR.PCL.Dungeon.IsUnnamedReign())
            {
                bonusHealth += (int) Math.ceil(m.maxHealth * 0.07);
            }
            else if (AbstractDungeon.ascensionLevel >= 7)
            {
                bonusHealth += (int) Math.ceil(m.maxHealth * 0.09);
            }
            else
            {
                bonusHealth += (int) Math.ceil(m.maxHealth * 0.13);
            }

            m.increaseMaxHp(bonusHealth, true);
        });
    }
}