package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class HeavyWarrior extends AnimatorCard implements OnCardResetListener
{
    public static final EYBCardData DATA = Register(HeavyWarrior.class)
            .SetAttack(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    private ColoredString magicNumberString = new ColoredString("X", Colors.Cream(1));

    public HeavyWarrior()
    {
        super(DATA);

        Initialize(27, 0);
        SetUpgrade(7, 0);

        SetAffinity_Red(2, 0, 8);

        SetAffinityRequirement(Affinity.Red, 4);
        SetExhaust(true);
    }

    @Override
    public ColoredString GetMagicNumberString()
    {
        return magicNumberString;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = GetHandAffinity(Affinity.Red);
        isMagicNumberModified = magicNumber > 0;
        magicNumberString = super.GetMagicNumberString();
    }

    @Override
    public void OnReset()
    {
        magicNumberString.SetText("X").SetColor(Colors.Cream(1));
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && JUtils.Any(player.hand.group, c -> c.uuid != uuid && c.costForTurn >= 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb).SetColor(Color.LIGHT_GRAY));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY)
        .SetVFXColor(Color.DARK_GRAY);

        if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS)
        {
            GameActions.Bottom.Motivate(1);
        }

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainForce(magicNumber);
        }
    }
}