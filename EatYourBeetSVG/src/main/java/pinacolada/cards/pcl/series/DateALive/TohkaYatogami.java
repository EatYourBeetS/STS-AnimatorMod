package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.InverseTohka;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class TohkaYatogami extends PCLCard
{
    public static final PCLCardData DATA = Register(TohkaYatogami.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Normal)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data -> data.AddPreview(new InverseTohka(), false));

    private boolean transformed;

    public TohkaYatogami()
    {
        super(DATA);

        Initialize(12, 0, 7, 1);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount - CombatStats.SynergiesThisCombat().size() * secondaryValue);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                Initialize(14, 0, 7, 1);
                SetUpgrade(0,0,0, 0);
            }
            else {
                SetHaste(true);
                Initialize(12, 0, 6, 1);
                SetUpgrade(0,0,0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        if (AbstractDungeon.player != null && !transformed && CheckSpecialCondition(true))
        {
            transformed = true;
            PCLActions.Bottom.MakeCardInHand(new InverseTohka()).SetUpgrade(upgraded, false);
            PCLActions.Last.Exhaust(this);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return (CombatStats.SynergiesThisCombat().size() >= magicNumber);
    }
}