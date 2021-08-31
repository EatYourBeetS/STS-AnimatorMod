package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.animator.DainsleifAbyssPower;
import eatyourbeets.utilities.GameActions;

public class Dainsleif extends AnimatorCard_UltraRare {
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static final EYBCardData DATA = Register(Dainsleif.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data ->
            {
                cardChoices.group.add(new Traveler_Aether());
                cardChoices.group.add(new Traveler_Lumine());
                for (AbstractCard c : cardChoices.group) {
                    data.AddPreview(c, true);
                    c = c.makeCopy();
                    c.upgrade();
                    upgradedCardChoices.group.add(c);

                }
            });

    public Dainsleif() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0);
        SetAffinity_Dark(2);
        SetAffinity_Blue(2);
        SetDelayed(true);

        SetAffinityRequirement(Affinity.General, 5);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.ChannelOrb(new Chaos());

        GameActions.Last.Callback(() ->
                GameActions.Bottom.SelectFromPile(name, CheckAffinity(Affinity.General) ? 2 : 1, upgraded ? upgradedCardChoices : cardChoices)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards) {
                                GameActions.Bottom.MakeCardInDrawPile(c);
                            }
                        }));
    }


    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.ApplyPower(player, player, new DainsleifAbyssPower(player));
    }
}