package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class KrulTepes extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(KrulTepes.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.UNCOMMON);

    private static final AbstractRelic relicReward = new BloodVial();

    public KrulTepes()
    {
        super(DATA);

        Initialize(12, 0, 2);
        SetUpgrade(4, 0, 0);
        SetScaling(0, 1, 2);

        
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(e -> GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40f * Settings.scale, Color.SCARLET.cpy())).duration)
            .AddCallback(enemy ->
            {
                AbstractRoom room = AbstractDungeon.getCurrRoom();
                if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                    && GameUtilities.IsFatal(enemy, false)
                    && CombatStats.TryActivateLimited(cardID))
                {
                    ObtainReward();
                }
            });

            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
    }

    public void ObtainReward()
    {
        int totalRelics = 0;
        ArrayList<AbstractRelic> relics = player.relics;
        for (AbstractRelic relic : relics)
        {
            if (relic.relicId.equals(relicReward.relicId))
            {
                totalRelics += 1;
            }
            else if (relic.relicId.equals(ExquisiteBloodVial.ID))
            {
                totalRelics = -1;
                break;
            }
        }

        if (totalRelics >= 5)
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new ExquisiteBloodVial());
        }
        else if (UnnamedReignRelic.IsEquipped())
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}