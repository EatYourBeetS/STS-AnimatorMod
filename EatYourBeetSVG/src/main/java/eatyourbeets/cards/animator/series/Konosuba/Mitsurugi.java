package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = Register(Mitsurugi.class);

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::updateCurrentEffect);

    public Mitsurugi()
    {
        super(ID, 0, CardRarity.COMMON, EYBAttackType.Normal);

        Initialize(8, 0, 1, 4);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetEffectPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            GameActions.Bottom.GainForce(magicNumber);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null || GameUtilities.IsAttacking(monster.intent))
        {
            cardText.OverrideDescription(null, true);
        }
        else
        {
            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
        }
    }
}