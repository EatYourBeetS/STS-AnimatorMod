package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.DivinityStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class FeridBathory extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeridBathory.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public static final int EXHAUST_PILE_THRESHOLD = 20;

    public FeridBathory()
    {
        super(DATA);

        Initialize(0,0, 3);
        SetUpgrade(0, 0, 3);

        SetAffinity_Dark(2);
        SetAffinity_Mind();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(DivinityStance.STANCE_ID);
        }

        GameActions.Bottom.StackPower(new FeridBathoryPower(p, magicNumber));
    }

    public class FeridBathoryPower extends AnimatorPower
    {
        public FeridBathoryPower(AbstractCreature owner, int amount)
        {
            super(owner, FeridBathory.DATA);

            Initialize(amount);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                    .SetDamageEffect(enemy ->
                    {
                        GameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY));
                        return 0f;
                    });
            GameActions.Bottom.GainTemporaryHP(amount);
            flashWithoutSound();
        }
    }
}