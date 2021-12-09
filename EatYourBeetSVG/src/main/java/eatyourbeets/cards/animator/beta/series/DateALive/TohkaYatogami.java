package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.InverseTohka;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class TohkaYatogami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TohkaYatogami.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data -> data.AddPreview(new InverseTohka(), false));

    private boolean transformed;

    public TohkaYatogami()
    {
        super(DATA);

        Initialize(14, 0, 6, 2);
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
                Initialize(14, 0, 6, 2);
                SetUpgrade(0,0,0, -1);
            }
            else {
                SetHaste(true);
                Initialize(14, 0, 6, 2);
                SetUpgrade(0,0,0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };


    @Override
    public void update()
    {
        super.update();

        if (AbstractDungeon.player != null && !transformed && CheckSpecialCondition(true))
        {
            transformed = true;
            GameActions.Bottom.MakeCardInHand(new InverseTohka()).SetUpgrade(upgraded, false);
            GameActions.Last.Exhaust(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return (CombatStats.SynergiesThisCombat().size() >= magicNumber);
    }
}