package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.replacement.AnimatorIntangiblePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SogaNoTojiko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SogaNoTojiko.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public SogaNoTojiko()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddPlayerIntangible();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AnimatorIntangiblePower(p, 1));
        GameActions.Bottom.TriggerOrbPassive(1).SetFilter(o -> Lightning.ORB_ID.equals(o.ID));
    }
}