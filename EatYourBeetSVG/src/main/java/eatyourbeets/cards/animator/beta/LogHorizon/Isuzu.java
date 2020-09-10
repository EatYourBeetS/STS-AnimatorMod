package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Isuzu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Isuzu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Isuzu()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ChooseStance();

        GameActions.Bottom.Callback(() -> {
            if (player.stance.ID.equals(AgilityStance.STANCE_ID))
            {
                GameActions.Bottom.GainAgility(magicNumber);
            }
            else if (player.stance.ID.equals(ForceStance.STANCE_ID))
            {
                GameActions.Bottom.GainForce(magicNumber);
            }
            else if (player.stance.ID.equals(IntellectStance.STANCE_ID))
            {
                GameActions.Bottom.GainIntellect(magicNumber);
            }
        });
    }

    private static void ChooseStance()
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)));
        group.addToBottom(CreateChoice(text[2], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID)));
        group.addToBottom(CreateChoice(text[3], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID)));

        GameActions.Top.SelectFromPile(Ainz.DATA.Strings.NAME, 1, group)
                .SetOptions(false, false)
                .SetMessage(CardRewardScreen.TEXT[1])
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        card.use(player, null);
                    }
                });
    }

    private static AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(Henrietta.DATA.ID)
                .SetProperties(CardType.SKILL, Henrietta.DATA.CardRarity, CardTarget.NONE)
                .SetCost(-2, 0)
                .SetOnUse(onSelect)
                .SetText(Ainz.DATA.Strings.NAME, text, text).Build();
    }
}