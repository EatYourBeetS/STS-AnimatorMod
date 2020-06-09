package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class YukariYakumo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YukariYakumo.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetExhaust(true);
        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);

        ArrayList<AbstractOrb> randomOrbs = new ArrayList<>();
        while (randomOrbs.size() < magicNumber)
        {
            AbstractOrb orb = GameUtilities.GetRandomOrb();
            while (orb instanceof Plasma)
            {
                orb = GameUtilities.GetRandomOrb(); //Don't let Plasma get picked
            }
            boolean dupe = false;
            for (AbstractOrb alreadyChosenOrbs : randomOrbs)
            {
                if (orb.ID.equals(alreadyChosenOrbs.ID))
                {
                    dupe = true;
                    break;
                }
            }
            if (!dupe)
            {
                randomOrbs.add(orb);
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(randomOrbs.get(0).name, (c1, p1, m1) ->
        {
            EvokeAndReplaceOrbs(randomOrbs.get(0));
        }));
        group.addToBottom(CreateChoice(randomOrbs.get(1).name, (c1, p1, m1) ->
        {
            EvokeAndReplaceOrbs(randomOrbs.get(1));
        }));
        group.addToBottom(CreateChoice(randomOrbs.get(2).name, (c1, p1, m1) ->
        {
            EvokeAndReplaceOrbs(randomOrbs.get(2));
        }));

        GameActions.Bottom.SelectFromPile(name, 1, group)
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

    private void EvokeAndReplaceOrbs(AbstractOrb chosenOrb)
    {
        GameActions.Bottom.Callback(() ->
        {
            int orbCount = 0;
            for (AbstractOrb orb : player.orbs)
            {
                if (!(orb instanceof EmptyOrbSlot))
                {
                    orbCount++;
                }
            }
            GameActions.Bottom.Add(new EvokeAllOrbsAction());
            for (int i = 0; i < orbCount; i++)
            {
                GameActions.Bottom.ChannelOrb(chosenOrb.makeCopy(), true);
            }
        });
    }

    private AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(cardID)
                .SetImage(assetUrl)
                .SetProperties(CardType.SKILL, rarity, CardTarget.NONE)
                .SetCost(-2, 0)
                .SetOnUse(onSelect)
                .SetText(name, text, text)
                .SetSynergy(synergy, false).Build();
    }
}

