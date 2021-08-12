package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.GenericEffects.GenericEffect_ChannelOrb;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;

public class IsshinKurosaki extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(IsshinKurosaki.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(0, 6, 1, 2);
        SetUpgrade(0, 3, 0);
        SetAffinity_Red(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_ChannelOrb(Fire::new, magicNumber));
            choices.AddEffect(new GenericEffect_GainStat(secondaryValue, PlayerAttribute.Force));
        }

        choices.Select(1, m);

        //if (CombatStats.TryActivateLimited(cardID)){
        //    GameActions.Last.Callback(cards ->
        //        GameActions.Bottom.Add(new ApplyAmountToOrbs(Fire.ORB_ID, 1))
        //    );
        //}
    }
}