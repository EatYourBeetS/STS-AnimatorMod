package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.General), false));

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0, 0, 2);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber));
    }

    public static class EvePower extends AnimatorPower implements OnAffinitySealedSubscriber
    {
        public EvePower(AbstractCreature owner, int amount)
        {
            super(owner, Eve.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAffinitySealed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAffinitySealed.Unsubscribe(this);
        }

        @Override
        public void OnAffinitySealed(EYBCard card, boolean manual)
        {
            GameEffects.Queue.BorderFlash(Color.SKY);
            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetOptions(true, false)
            .SetDamageEffect(enemy ->
            {
                SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
                GameEffects.List.Add(VFX.SmallLaser(owner.hb, enemy.hb, Color.CYAN));
                return 0f;
            });

            this.flash();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            if (CombatStats.TryActivateLimited(Eve.DATA.ID))
            {
                GameActions.Last.Callback(() ->
                {
                    GameActions.Bottom.Reload(name, cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            GameActions.Bottom.ObtainAffinityToken(Affinity.Star, false);
                        }
                    });
                    flash();
                });
            }
        }
    }
}