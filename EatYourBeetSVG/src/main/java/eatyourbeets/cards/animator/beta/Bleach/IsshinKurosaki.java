package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_ChannelOrb;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;

public class IsshinKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsshinKurosaki.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(0, 6, 1, 2);
        SetUpgrade(0, 3, 0);
        SetMartialArtist();

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_ChannelOrb(magicNumber, new Fire()));
            choices.AddEffect(new GenericEffect_GainStat(secondaryValue, PlayerAttribute.Force));
        }

        choices.Select(secondaryValue, m);

        if (CombatStats.TryActivateSemiLimited(cardID)){
            GameActions.Bottom.Callback(cards -> {
                BoostAllFireOrbs();
            });
        }
    }

    private void BoostAllFireOrbs()
    {
        for (AbstractOrb orb : player.orbs)
        {
            if (Fire.ORB_ID.equals(orb.ID))
            {
                orb.passiveAmount = orb.passiveAmount + magicNumber;
                orb.evokeAmount = magicNumber;
            }
        }
    }
}