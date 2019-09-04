package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.HexPower;
import com.megacrit.cardcrawl.powers.RagePower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.actions.common.AddToCardGroupAction;
import eatyourbeets.actions.common.PurgeAnywhereAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class Cthulhu extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Cthulhu.class.getSimpleName());

    public Cthulhu()
    {
        super(ID, 3, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 100);

        this.purgeOnUse = true;

        SetSynergy(Synergies.CallOfCthulhu);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1f));
        GameActionsHelper.AddToTop(new VFXAction(new BorderLongFlashEffect(Color.valueOf("3d0066"))));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_DARK_EVOKE", 0.1f));

        PurgeAllCards(p.hand);
        PurgeAllCards(p.drawPile);
        PurgeAllCards(p.discardPile);
        PurgeAllCards(p.exhaustPile);

        GenerateRandomCards();

        HigakiRinne rinne = new HigakiRinne(); // Rinne is Cthulhu confirmed
        for (int i = 0; i < 16; i++)
        {
            GameActionsHelper.AddToBottom(new HigakiRinneAction(rinne));
        }

        GameActionsHelper.PurgeCard(this);
        GameActionsHelper.ApplyPowerSilently(p, p, new ConfusionPower(p), 1);
        GameActionsHelper.ApplyPowerSilently(p, p, new HexPower(p, 1), 1);
        GameActionsHelper.ApplyPowerSilently(p, p, new RagePower(p, 3), 3);

        GameActionsHelper.DrawCard(p, 6);
        GameActionsHelper.GainEnergy(3);

        for (int i = 0; i < 16; i++)
        {
            GameActionsHelper.AddToBottom(new HigakiRinneAction(rinne));
        }
    }

    private void PurgeAllCards(CardGroup group)
    {
        for (AbstractCard card : group.group)
        {
            GameActionsHelper.AddToBottom(new PurgeAnywhereAction(card, null, 1));
            GameActionsHelper.SFX("CARD_EXHAUST");
        }
    }

    private void GenerateRandomCards()
    {
        ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
        ArrayList<AbstractCard> cards = new ArrayList<>();

        for (int i = 0; i < magicNumber; i++)
        {
            String key = Utilities.GetRandomElement(keys);

            AbstractCard card = CardLibrary.cards.get(key).makeCopy();
            while (card.canUpgrade() && AbstractDungeon.cardRandomRng.randomBoolean(0.3f))
            {
                card.upgrade();
            }
            cards.add(card);
        }

        GameActionsHelper.AddToBottom(new AddToCardGroupAction(cards, AbstractDungeon.player.drawPile));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}