package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.*;

public class SanaFutaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SanaFutaba.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public SanaFutaba()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Green(1);

        SetEthereal(true);

        SetCardPreview(SanaFutaba::Filter);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetHaste(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Wait(GameEffects.List.ShowCopy(this, Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.5f).duration * 0.35f);
        GameActions.Bottom.GainBlock(GameUtilities.GetEnemies(true).size() * secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, 1, p.drawPile)
        .SetOptions(CardSelection.Top, true)
        .SetFilter(SanaFutaba::Filter)
        .AddCallback(cards ->
        {
            final int max = cards.size();
            if (max > 0)
            {
                for (int i = 0; i < max; i++)
                {
                    final AbstractCard c = cards.get(i);
                    BlockModifiers.For(c).Add(cardID, magicNumber);
                    GameEffects.List.ShowCopy(c, Settings.WIDTH * 0.25f + (i * AbstractCard.IMG_WIDTH * 0.6f), Settings.HEIGHT * 0.4f);
                }

                SFX.Play(SFX.BLOCK_GAIN_2, 2);
            }
        });
    }

    private static boolean Filter(AbstractCard card)
    {
        return card.type == CardType.SKILL && card.baseBlock >= 0;
    }
}