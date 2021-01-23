package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.List;

public class CrumblingOrb extends AnimatorRelic
{
    public static final String ID = CreateFullID(CrumblingOrb.class);

    public CrumblingOrb()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        GameActions.Bottom.Callback(() ->
        {
            GameActions.Bottom.GainEnergy(1);
            flash();
        });
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            List<AbstractCard> cardsToChange = new ArrayList();

            RandomizedList<AbstractCard> changeableCards = new RandomizedList<>();
            for (AbstractCard c : player.masterDeck.group)
            {
                if (!GameUtilities.IsCurseOrStatus(c))
                {
                    changeableCards.Add(c);
                }
            }

            float displayCount = 0f;

            for (int i=0; i<2; i++)
            {
                if (changeableCards.Size() > 0)
                {
                    AbstractCard currentCard = changeableCards.Retrieve(rng);

                    if (currentCard != null)
                    {
                        boolean upgraded = currentCard.upgraded;

                        AbstractDungeon.player.masterDeck.removeCard(currentCard);

                        AbstractCard reward = AbstractDungeon.returnTrulyRandomCard();

                        if (reward != null)
                        {
                            reward = reward.makeCopy();

                            if (upgraded)
                            {
                                reward.upgrade();
                            }

                            GameEffects.TopLevelQueue.Add(new ShowCardAndObtainEffect(reward, (float) Settings.WIDTH / 3f + displayCount, (float) Settings.HEIGHT / 2f, false));
                            displayCount += (float) Settings.WIDTH / 6f;
                        }
                    }
                }
            }
        }

        flash();
    }
}