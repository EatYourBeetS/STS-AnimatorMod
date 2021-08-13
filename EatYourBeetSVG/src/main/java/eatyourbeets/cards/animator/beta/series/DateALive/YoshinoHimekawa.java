package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Zadkiel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class YoshinoHimekawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();
    private boolean transformed = false;
    static
    {
        DATA.AddPreview(new YoshinoHimekawa(true), true);
        DATA.AddPreview(new Zadkiel(), true);
    }

    private YoshinoHimekawa(boolean transformed)
    {
        this();

        SetTransformed(transformed);
    }

    public YoshinoHimekawa()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);
        SetAffinity_Green(2, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
        SetHaste(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void triggerOnExhaust()
    {
        if (!transformed && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Top.MakeCardInDiscardPile(new Zadkiel()).SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (!transformed)
        {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
                    .AddCallback(() -> {
                        SetTransformed(true);
                        GameActions.Bottom.GainBlur(secondaryValue);
                    })
                    .SetDuration(0.15f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlur(secondaryValue);
        GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber).ShowEffect(false, true);
    }

    private void SetTransformed(boolean value)
    {
        transformed = value;

        if (transformed)
        {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
        else
        {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
        }
    }
}