package eatyourbeets.cards.animatorClassic.colorless.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
import eatyourbeets.cards.animatorClassic.special.OrbCore;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.*;

import java.util.HashSet;

public class Patchouli extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Patchouli.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.Random).SetColor(CardColor.COLORLESS);
    static
    {
        for (OrbCore core : OrbCore.GetAllCores())
        {
            DATA.AddPreview(core, false);
        }
    }

    private final HashSet<String> uniqueOrbs = new HashSet<>();

    public Patchouli()
    {
        super(DATA);

        Initialize(7, 0, 1, 2);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(2, 0, 0);

        SetSeries(CardSeries.TouhouProject);
        SetSpellcaster();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        uniqueOrbs.clear();

        for (AbstractOrb orb : AbstractDungeon.actionManager.orbsChanneledThisCombat)
        {
            uniqueOrbs.add(orb.ID);
        }

        GameUtilities.IncreaseMagicNumber(this, uniqueOrbs.size(), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        RandomizedList<ActionT0> actions = new RandomizedList<>();
        for (int i = 0; i < magicNumber; i++)
        {
            if (actions.Size() == 0)
            {
                actions.Add(this::Aether);
                actions.Add(this::Fire);
                actions.Add(this::Frost);
                actions.Add(this::Lightning);
            }

            actions.Retrieve(rng).Invoke();
            GameActions.Bottom.WaitRealtime(0.2f);
        }
    }

    private void Lightning()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.2f);
            return GameEffects.Queue.Add(new LightningEffect(e.drawX, e.drawY)).duration;
        });
    }

    private void Frost()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - (float) frostCount / 200f);
            for (int f = 0; f < frostCount; f++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }

            return frostCount * 0.1f;
        });
    }

    private void Aether()
    {
        CreateDamageAction().SetDamageEffect(__ ->
        {
            CardCrawlGame.sound.play("ATTACK_WHIRLWIND", 0.2f);
            return GameEffects.Queue.Add(new WhirlwindEffect()).duration;
        });
    }

    private void Fire()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
            return GameEffects.Queue.Add(new FireballEffect(player.hb.cX, player.hb.cY, e.hb.cX, e.hb.cY)).duration;
        });
    }

    private DealDamageToRandomEnemy CreateDamageAction()
    {
        return GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE).SetOptions(true, false);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && CombatStats.TryActivateLimited(cardID))
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.MakeCardInDiscardPile(JUtils.Random(OrbCore.GetAllCores()).makeCopy());
        }
    }
}