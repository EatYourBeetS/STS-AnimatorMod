package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Greed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Greed.class).SetPower(2, CardRarity.RARE).SetMaxCopies(1);

    private static final CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final int BLOCK = 7;
    private static final int TEMP_HP = 6;
    private static final int METALLICIZE = 3;
    private static final int PLATED_ARMOR = 4;
    private static final int MALLEABLE = 6;

    public Greed()
    {
        super(DATA);

        Initialize(0,0, 200, 1);
        SetUpgrade(0,0, -50);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public String GetRawDescription()
    {
        return super.GetRawDescription(BLOCK, TEMP_HP, METALLICIZE, PLATED_ARMOR, MALLEABLE);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseSecondaryValue(this, Math.floorDiv(player.gold, magicNumber), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (choices.isEmpty())
        {
            choices.addToTop(CreateChoice(BLOCK, GR.Tooltips.Block, this::Effect1));
            choices.addToTop(CreateChoice(TEMP_HP, GR.Tooltips.TempHP, this::Effect2));
            choices.addToTop(CreateChoice(METALLICIZE, GR.Tooltips.Metallicize, this::Effect3));
            choices.addToTop(CreateChoice(PLATED_ARMOR, GR.Tooltips.PlatedArmor, this::Effect4));
            choices.addToTop(CreateChoice(MALLEABLE, GR.Tooltips.Malleable, this::Effect5));
        }

        if (secondaryValue >= choices.size())
        {
            for (AbstractCard card : choices.group)
            {
                card.use(player, m);
            }
        }
        else
        {
            GameActions.Bottom.SelectFromPile(name, secondaryValue, choices)
            .SetOptions(false, false)
            .AddCallback(m, (target, cards) ->
            {
                for (AbstractCard card : cards)
                {
                    card.use(player, target);
                }
            });
        }
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(BLOCK);
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(TEMP_HP);
    }

    private void Effect3(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainMetallicize(METALLICIZE);
    }

    private void Effect4(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainPlatedArmor(PLATED_ARMOR);
    }

    private void Effect5(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainMalleable(MALLEABLE);
    }

    private AnimatorCard_Dynamic CreateChoice(int amount, EYBCardTooltip buff, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(this, GR.Animator.Strings.Actions.GainAmount(amount, "["+buff+"]", true), false).SetOnUse(onSelect).Build();
    }
}