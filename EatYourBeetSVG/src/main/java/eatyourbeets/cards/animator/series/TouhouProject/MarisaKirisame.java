package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(2, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1, 1, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.ShootingStars(player.hb, player.hb.height));
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE);

        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Fire(this));
        choices.AddEffect(new GenericEffect_Lightning(this));
        choices.Select(1, m);
    }

    protected static class GenericEffect_Fire extends GenericEffect
    {
        private final EYBCard source;

        public GenericEffect_Fire(MarisaKirisame source)
        {
            this.source = source;
        }

        @Override
        public String GetText()
        {
            return source.cardData.Strings.EXTENDED_DESCRIPTION[0];
        }

        @Override
        public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.TriggerOrbPassive(1).SetFilter(Fire.class::isInstance);
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }

    protected static class GenericEffect_Lightning extends GenericEffect
    {
        private final EYBCard source;

        public GenericEffect_Lightning(MarisaKirisame source)
        {
            this.source = source;
        }

        @Override
        public String GetText()
        {
            return source.cardData.Strings.EXTENDED_DESCRIPTION[1];
        }

        @Override
        public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.TriggerOrbPassive(1).SetFilter(Lightning.class::isInstance);
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }
}

