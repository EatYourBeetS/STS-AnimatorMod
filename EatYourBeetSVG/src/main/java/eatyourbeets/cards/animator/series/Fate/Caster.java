package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class).SetSkill(1, CardRarity.UNCOMMON);

    private static final CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    public void triggerOnExhaust()
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        if (enemies.size() == 1)
        {
            ChooseAction(enemies.get(0));
        }
        else
        {
            ChooseAction(null);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Dark(), true);

        if (HasSynergy())
        {
            ChooseAction(m);
        }
    }

    public void ChooseAction(AbstractMonster m)
    {
        if (choices.isEmpty())
        {
            String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
            choices.addToBottom(CreateChoice(text[0], this::Effect1));
            choices.addToBottom(CreateChoice(JUtils.Format(text[1], magicNumber), this::Effect2));
            choices.addToBottom(CreateChoice(text[2], this::Effect3));
        }

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

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.EvokeOrb(1);
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.IsValidTarget(m))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
        else
        {
            GameActions.Bottom.SelectCreature(SelectCreature.Targeting.Enemy, card.name)
            .SetMessage(card.rawDescription)
            .AddCallback(m1 -> GameActions.Top.ReduceStrength(m1, magicNumber, true));
        }
    }

    private void Effect3(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(1);
        GameActions.Bottom.GainIntellect(1);
    }

    private AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(this, text, false).SetOnUse(onSelect).Build();
    }
}