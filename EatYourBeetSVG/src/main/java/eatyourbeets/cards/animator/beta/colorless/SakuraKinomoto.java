package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SakuraKinomoto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuraKinomoto.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CardcaptorSakura);

    public SakuraKinomoto()
    {
        super(DATA);

        Initialize(17, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetAffinity_Blue(2, 0 ,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
                    .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PINK)).duration * 0.1f)
            .AddCallback(enemy ->
            {
                AbstractRoom room = AbstractDungeon.getCurrRoom();
                if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                && GameUtilities.IsFatal(enemy, false)
                && CombatStats.TryActivateLimited(cardID))
                {
                    RewardItem reward = new RewardItem();
                    CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    // Copies of this card won't appear in card rewards
                    ArrayList<AbstractCard> ignored = new ArrayList<>();
                    ignored.add(this);
                    for (int i = 0; i < magicNumber; i++)
                    {
                        AbstractCard card = GR.Common.Dungeon.GetRandomRewardCard(ignored, false, true);
                        reward.cards.add(card);
                        choices.addToBottom(card);
                    }

                    GameActions.Bottom.SelectFromPile(name, 1, choices)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0).makeCopy();
                            GameActions.Top.MakeCard(card, player.masterDeck);
                        }
                    });
                }
            });
            GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE");
        }
    }

}