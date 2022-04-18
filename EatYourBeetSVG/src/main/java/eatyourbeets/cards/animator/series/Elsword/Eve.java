package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (EYBCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), false);
                }
            });

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber));
    }

    public static class EvePower extends AnimatorPower
    {
        public EvePower(AbstractCreature owner, int amount)
        {
            super(owner, Eve.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.Add(AffinityToken.SelectTokenAction(name, false, false, 1, 3)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.MakeCardInHand(c);
                }
            }));
        }

//        @Override
//        public void onAfterCardPlayed(AbstractCard usedCard)
//        {
//            super.onAfterCardPlayed(usedCard);
//
//            if (CombatStats.Affinities.IsSynergizing(usedCard))
//            {
//                final int damage = CombatStats.Affinities.GetHandAffinityLevel(Affinity.General, usedCard);
//                if (damage > 0)
//                {
//                    //GameEffects.Queue.BorderFlash(Color.SKY);
//                    for (int i = 0; i < amount; i++)
//                    {
//                        GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
//                        .SetOptions(true, false)
//                        .SetDamageEffect(enemy ->
//                        {
//                            SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
//                            GameEffects.List.Add(VFX.SmallLaser(owner.hb, enemy.hb, Color.CYAN));
//                            return 0f;
//                        });
//                    }
//
//                    this.flash();
//                    this.updateDescription();
//                }
//            }
//        }
    }
}