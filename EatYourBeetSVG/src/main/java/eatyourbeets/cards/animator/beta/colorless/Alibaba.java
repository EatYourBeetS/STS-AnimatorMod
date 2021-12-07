package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.beta.special.Alibaba_Aladdin;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_ApplyToAll;
import eatyourbeets.misc.GenericEffects.GenericEffect_TriggerOrb;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Alibaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alibaba.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Normal)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Magi)
            .PostInitialize(data -> data.AddPreview(new Alibaba_Aladdin(), false));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Alibaba()
    {
        super(DATA);

        Initialize(3, 0, 2 , 2);
        SetUpgrade(0, 0, 0 , 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Red, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL).forEach(d -> d.AddCallback(e -> {
            if (e.lastDamageTaken > 0) {
                if (choices.TryInitialize(this))
                {
                    choices.AddEffect(new GenericEffect_ApplyToAll(TargetHelper.Normal(e), PowerHelper.Burning, magicNumber));
                    choices.AddEffect(new GenericEffect_TriggerOrb(new Earth()));
                }
                choices.Select(1, m);
            }
        }));

        int uniqueRareOrbs = 0;
        for (AbstractOrb orb : player.orbs)
        {
            if (GameUtilities.IsValidOrb(orb) && !GameUtilities.IsCommonOrb(orb))
            {
                uniqueRareOrbs += 1;
            }
        }
        if (uniqueRareOrbs >= secondaryValue && info.TryActivateLimited()) {
            GameActions.Bottom.MakeCardInHand(new Alibaba_Aladdin());
            GameActions.Last.Exhaust(this);
        }
    }

}