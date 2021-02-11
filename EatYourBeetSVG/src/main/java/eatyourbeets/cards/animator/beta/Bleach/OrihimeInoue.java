package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrihimeInoue extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 0, 5, 8);
        SetUpgrade(0, 0, 0);
        SetEthereal(true);
        SetExhaust(true);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(card -> {
            ChooseAction(m);
        });
    }

    private void ChooseAction(AbstractMonster m)
    {
        AnimatorCard block = GenerateInternalBlock(CardType.SKILL, this::BlockEffect).Build();
        AnimatorCard tempHP = GenerateInternalTempHP(CardType.SKILL, this::TempHPEffect).Build();
        AnimatorCard evokeBurn = GenerateInternalEvokeBurn(CardType.SKILL, this::EvokeAndBurnEffect).Build();

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (ContainsCardInHandWithFilter(card -> card.costForTurn >= 0 && card.costForTurn <= 1))
        {
            choices.addToTop(block);
        }
        if (ContainsCardInHandWithFilter(card -> card.costForTurn >= 2))
        {
            choices.addToTop(tempHP);
        }
        if (ContainsCardInHandWithFilter(card -> card.costForTurn < 0))
        {
            choices.addToTop(evokeBurn);
        }

        if (choices.size() > 0)
        {
            Execute(choices, m);
        }
    }

    private AnimatorCardBuilder GenerateInternalBlock(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaKuchiki.DATA.ID);
        builder.SetText(name, "", "");
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);
        builder.SetNumbers(0, secondaryValue, 0, 0);

        return builder;
    }

    private AnimatorCardBuilder GenerateInternalTempHP(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaKuchiki.DATA.ID);
        builder.SetText(name, "", "");
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);
        builder.SetNumbers(0, 0, magicNumber, 0);
        builder.SetSpecialInfo(c -> TempHPAttribute.Instance.SetCard(c, true));

        return builder;
    }

    private AnimatorCardBuilder GenerateInternalEvokeBurn(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaKuchiki.DATA.ID);
        builder.SetText(name, DATA.Strings.EXTENDED_DESCRIPTION[0], DATA.Strings.EXTENDED_DESCRIPTION[0]);
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        return builder;
    }

    private void Execute(CardGroup group, AbstractMonster m)
    {
        GameActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.get(0);
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    card.use(player, m);
                });
    }

    private boolean ContainsCardInHandWithFilter(Predicate<AbstractCard> filter)
    {
        return player.hand.group.stream().filter(filter)
                .collect(Collectors.toList()).size() > 0;
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(secondaryValue);
    }

    private void TempHPEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    private void EvokeAndBurnEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        AbstractOrb fireToEvoke = null;

        for (AbstractOrb orb : player.orbs)
        {
            if (Fire.ORB_ID.equals(orb.ID))
            {
                fireToEvoke = orb;
                break;
            }
        }

        if (fireToEvoke != null)
        {
            GameActions.Bottom.EvokeOrb(3, fireToEvoke);

            for (int i=0; i<2; i++)
            {
                GameActions.Bottom.MakeCardInDrawPile(new Burn());
            }
        }
    }
}