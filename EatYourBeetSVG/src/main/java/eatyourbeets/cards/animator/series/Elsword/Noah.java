package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Noah extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(Noah.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(7, 0, 1, 4);
        SetUpgrade(0, 0, 1,1);

        SetAffinity_Dark();

        SetAffinityRequirement(Affinity.Dark, 13);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f);

        if (CheckAffinity(Affinity.Dark))
        {
            GameActions.Bottom.ApplyPower(new DoubleTapPower(p, magicNumber));
        }

        int numDarkInExhaust = 0;

        for (AbstractCard card : p.exhaustPile.group)
        {
            if (GameUtilities.HasAffinity(card, Affinity.Dark))
            {
                numDarkInExhaust++;
            }
        }

        GameActions.Bottom.RaiseDarkLevel(secondaryValue * numDarkInExhaust);
    }
}