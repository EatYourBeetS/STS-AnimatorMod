package eatyourbeets.cards.animator.special;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Konosuba.Sylvia;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class Sylvia_Chimera extends AnimatorCard implements OnCardResetListener
{
    public static final EYBCardData DATA = Register(Sylvia_Chimera.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetSeries(Sylvia.DATA.Series)
            .SetMaxCopies(1);

    private ColoredString magicNumberString = new ColoredString("X", Colors.Cream(1));

    public Sylvia_Chimera()
    {
        super(DATA);

        Initialize(12, 7);
        SetUpgrade(2, 2);

        SetAffinity_Star(2, 0, 1);

        SetRetainOnce(true);
    }

    @Override
    public ColoredString GetMagicNumberString()
    {
        return magicNumberString;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = GetHandAffinity(Affinity.General, true);
        isMagicNumberModified = magicNumber > 0;
        magicNumberString = super.GetMagicNumberString();
    }

    @Override
    public void OnReset()
    {
        magicNumberString.SetText("X").SetColor(Colors.Cream(1));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        final int amount = GetHandAffinity(Affinity.General, true);
        if (amount > 0)
        {
            GameActions.Bottom.ApplyPoison(player, m, amount);
            GameActions.Bottom.GainPlatedArmor(amount);
        }

        GameActions.Bottom.ModifyAffinityLevel(player.hand, BaseMod.MAX_HAND_SIZE, Affinity.General, -1, true)
        .Flash(Color.RED)
        .SetFilter(c -> c.uuid != uuid)
        .SetDuration(0.05f, true);
    }
}